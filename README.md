# RStore

## 简介
使用观察者模式观察属性变化，并且通过监听可以进行界面更新等操作。

## 使用
**viewmodel中**

```
class MainViewModel(application: Application, handle: SavedStateHandle) :
    SaveStateStoreViewModel(application, handle) {
    val countOb by property(::count,isAnchorProperty = true){
        "$this"
    }
    var count by property(0, shouldSaveState = true)
    val data by flowProperty(count, isAnchorProperty = true, shouldSaveState = true)

    var list by listProperty<String>(isAnchorProperty = true)
}
```

**fragment中**
```
class TestFragment : Fragment(R.layout.fragment_test), StoreComponent {

    private val viewModel by viewModels<MainViewModel>()

    private var position = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = arguments?.getInt("position", position) ?: 0
    }
    
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewBinding = FragmentTestBinding.bind(view)

        viewModel.withAnchor(starter = LifecycleAnchorStarter(viewModel.data.value > 0)) {
            with(it) {
                onInitialized {
                    viewBinding.root.setOnClickListener {
                        data.tryEmit(++count)
                    }
                }
                stareAt(::data) {
                    viewBinding.data.text = "data $position =${data.value}"
                }
            }
        }

    }
}
```
**activity中**
class MainActivity : AppCompatActivity(), StoreComponent {
    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    private val adapter by lazy {
        MainFragmentFragmentStateAdapter(supportFragmentManager, lifecycle)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewPager.adapter = adapter
        setState(binding)
    }

    private fun setState(binding: ActivityMainBinding) {

        with(viewModel) {
            withAnchor(starter = LifecycleAnchorStarter()) {
                onInitialized {
                    binding.root.setOnClickListener {
                        ++count
                    }
                }           
                stareAt(::countOb) {
                    println("enter this line countOb=$this")
                    binding.data.text = "data=$this"
                }
                stareAt(::list) {
                    println("enter this line list=$this")
                }
            }
        }
        lifecycleScope.launch {
            with(viewModel) {
                awaitUntil(::data) {
                    value > 5
                }
            }
            Toast.makeText(this@MainActivity, "data 数值大于5", Toast.LENGTH_SHORT).show()
        }

    }
}
