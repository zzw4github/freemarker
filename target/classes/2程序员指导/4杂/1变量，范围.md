本章解释了模板尝试访问变量时会发生什么，以及变量的存储方式。

当你调用`Template.process`它时，它将在内部创建一个`Environment`将被使用的对象，直到`process`返回。该对象存储模板处理的运行时状态。除其他事项外，它存储通过与像指令模板创建的变量 `assign`，`macro`， `local`或`global`。它永远不会尝试修改传递给`process`调用的数据模型对象 ，也不会创建或替换存储在配置中的共享变量。

当您尝试读取变量时，FreeMarker将按此顺序搜索变量，并在找到具有正确名称的变量时停止：

1. 在环境中：
   1. 如果你在一个循环中，在循环变量集中。循环变量是由指令创建的变量 `list`。
   2. 如果您在宏内部，则在宏的局部变量集中。可以使用该`local`指令创建局部变量 。此外，宏的参数是局部变量。
   3. 在当前的[命名空间中](https://freemarker.apache.org/docs/dgui_misc_namespace.html)。您可以使用该`assign` 指令将变量放入命名空间。
   4. 在使用`global`指令创建的变量集中 。FTL处理这些变量就好像它们是数据模型的正常成员一样。也就是说，它们在所有名称空间中都是可见的，您可以像访问数据模型一样访问它们。
2. 在已传递给`process`方法的数据模型对象中
3. 在存储的共享变量集中 `Configuration`

实际上，从模板作者的角度来看，这6层只有4层，因为从那个角度来看，最后3层（用`global`实际数据模型对象创建的变量，共享变量）一起构成了全局变量集。

请注意，可以使用[特殊变量](https://freemarker.apache.org/docs/ref_specvar.html)从FTL中的特定图层获取[变量](https://freemarker.apache.org/docs/ref_specvar.html)。