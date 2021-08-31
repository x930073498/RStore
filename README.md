# RStore

## 简介
使用观察者模式观察属性变化，并且通过监听可以进行界面更新等操作。

## 使用
**viewmodel中**

```
//如果属性需要监听需使用下面类似的方式初始化属性（库里提供的代理属性方法）
class MainViewModel(application: Application, handle: SavedStateHandle) :
    SaveStateStoreViewModel(application, handle) {
    //isAnchorProperty 与StoreComponent 中的viewmodel.withAnchor 配合使用，true表示可以监听
    val countOb by property(::count,isAnchorProperty = true){
        "$this"
    }
    //shouldSaveState 是否使用savedState缓存对象
    var count by property(0, shouldSaveState = true)
    val data by flowProperty(count, isAnchorProperty = true, shouldSaveState = true)

    var list by listProperty<String>(isAnchorProperty = true)
}
```

**fragment中**
```
//fragment（activity实现StoreComponent，不要复写任何方法）
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

//监听viewmodel 中isAnchorProperty=true的属性
//starter 用于控制流程开始的时机，默认在StoreComponent 进入onResume周期时开始进入流程（监听代码块开始生效）
        viewModel.withAnchor(starter = LifecycleAnchorStarter(viewModel.data.value > 0)) {
            //这里面的代码块会运行多次，不要在里面直接写逻辑
            with(it) {

                onInitialized {
                           //初始化时运行，只会运行一次
                    viewBinding.root.setOnClickListener {
                        data.tryEmit(++count)
                    }
                }
                //监听data属性的变化
                stareAt(::data) {
                //属性变化时执行
                    viewBinding.data.text = "data $position =${data.value}"
                }
            }
        }

    }
}
```
**activity中（与fragment中的使用方法一致）**
```
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
```
