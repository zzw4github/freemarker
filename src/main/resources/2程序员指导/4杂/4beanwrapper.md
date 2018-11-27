# 豆包装

页面内容

- [TemplateHashModel功能](https://freemarker.apache.org/docs/pgui_misc_beanwrapper.html#beanswrapper_hash)
- [关于安全的一个词](https://freemarker.apache.org/docs/pgui_misc_beanwrapper.html#autoid_54)
- [TemplateScalarModel功能](https://freemarker.apache.org/docs/pgui_misc_beanwrapper.html#autoid_55)
- [TemplateNumberModel功能](https://freemarker.apache.org/docs/pgui_misc_beanwrapper.html#autoid_56)
- [TemplateCollectionModel功能](https://freemarker.apache.org/docs/pgui_misc_beanwrapper.html#autoid_57)
- [TemplateSequenceModel功能](https://freemarker.apache.org/docs/pgui_misc_beanwrapper.html#autoid_58)
- [TemplateMethodModel功能](https://freemarker.apache.org/docs/pgui_misc_beanwrapper.html#beanswrapper_method)
- [解开规则](https://freemarker.apache.org/docs/pgui_misc_beanwrapper.html#autoid_59)
- [访问静态方法](https://freemarker.apache.org/docs/pgui_misc_beanwrapper.html#autoid_60)
- [访问枚举](https://freemarker.apache.org/docs/pgui_misc_beanwrapper.html#jdk_15_enums)

注意：

`BeansWrapper`不建议再使用直接使用。`DefaultObjectWrapper`相反，请使用其子类， 但要确保其`incompatibleImprovements`属性至少为2.3.22。`DefaultObjectWrapper`提供更清晰的数据模型（更少混淆的多类型FTL值）并且通常更快。[在这里查看有关DefaultObjectWrapper的更多信息......](https://freemarker.apache.org/docs/pgui_datamodel_objectWrapper.html#pgui_datamodel_defaultObjectWrapper)

这`freemarker.ext.beans.BeansWrapper`是一个 最初添加到FreeMarker 的 [对象包装器](https://freemarker.apache.org/docs/pgui_datamodel_objectWrapper.html)，因此可以将任意POJO-s（Plain Old Java Objects）包装到`TemplateModel` 接口中。从那时起它已成为正常的做事方式，事实上它`DefaultObjectWrapper`本身就是一种`BeansWrapper`延伸。因此，这里描述的一切都为`DefaultObjectWrapper`过，除了`DefaultObjectWrapper`将包裹 `String`，`Number`，`Date`，`array`， `Collection`（像`List`） `Map`，`Boolean`并 `Iterator`与对象 类和W3C DOM节点用 （[更多关于包裹W3C DOM ...](https://freemarker.apache.org/docs/xgui.html)`freemarker.template.Simple*Xxx*``freemarker.ext.dom.NodeModel`），因此对于那些上述规则不适用。

你会想要使用`BeansWrapper`而不是 `DefaultObjectWrapper`任何这些立场：

- 应该允许在模板执行期间修改模型的`Collection`-s和 `Map`-s。（`DefaultObjectWrapper`防止这种情况，因为它在包装时会创建集合的副本，并且副本将是只读的。）
- 如果身份`array`， `Collection`并且`Map`当它们被传递到包装对象的模板方法的对象必须保持。也就是说，如果这些方法必须获得与先前包装的完全相同的对象。
- 如果前面列出类的的Java API（ ，， `String` ...等）应为模板可见。即使使用默认情况下它们也不可见，但可以通过设置曝光级别来实现（参见后面的内容）。请注意，这通常是一种不好的做法; 尝试使用[内置的插件](https://freemarker.apache.org/docs/ref_builtins.html)（如 ，， 而不是Java的API ...等）。`Map``List``BeansWrapper``foo?size``foo?upper_case``foo?replace('_', '-')`

以下是创建的`TemplateModel`-s 的摘要`BeansWrapper`。假设`obj`在包装之前调用该对象，并且 `model`在包装之后为了下面的讨论而调用该对象。

## TemplateHashModel功能

每个对象都将被包装成一个 `TemplateHashModel`暴露JavaBeans属性和对象方法的对象。这样，您就可以 `model.foo`在模板中使用调用`obj.getFoo()`或`obj.isFoo()` 方法。（请注意，公共字段不是直接可见的;您必须为它们编写一个getter方法。）公共方法也可以通过哈希模型作为模板方法模型检索，因此您可以使用它`model.doBar()`来调用 `object.doBar()`。关于方法模型功能的讨论的更多内容。

如果请求的密钥无法映射到bean属性或方法，则框架将尝试定位所谓的“泛型get方法”，即具有签名public `*any-return-type* get(String)`或public的 `*any-return-type* get(Object)`方法，并使用所请求的密钥调用该方法。请注意，这允许方便地访问`java.util.Map`类和类中的映射 - 只要地图的键是`String`s并且某些属性或方法名称不会影响映射。（有一种避免阴影的解决方案，请继续阅读。）另请注意，`java.util.ResourceBundle`对象模型 使用 `getObject(String)`通用get方法。

如果你调用`setExposeFields(true)`一个 `BeansWrapper`实例，它也将暴露的类作为哈希键和值公众，非静态字段。即，如果`foo`是类的一个公共的，非静态字段 `Bar`，并且`bar`是一个模板变量包装的一个实例`Bar`，则 `bar.foo`表达式会作为字段的值`foo`的的`bar` 对象。该类的所有超类中的公共字段也被暴露。

## 关于安全的一个词

默认情况下，您将无法访问被认为不适合模板化的多种方法。例如，您不能使用同步方法（`wait`， `notify`，`notifyAll`），线程和线程组管理方法（`stop`， `suspend`，`resume`， `setDaemon`，`setPriority`），反射（方法 ， ， ， 等）和各种危险的方法和 类（，， ，，等等。）。该 有几个安全级别（称为“方法暴露水平”），并称为默认 `Field``set*Xxx*``Method.invoke``Constructor.newInstance``Class.newInstance``Class.getClassLoader``System``Runtime``exec``exit``halt``load``BeansWrapper``EXPOSE_SAFE`可能适合大多数应用程序。有一个无保护级别调用 `EXPOSE_ALL`，允许你甚至调用上面的不安全方法，以及一个`EXPOSE_PROPERTIES_ONLY`只暴露bean属性getter 的严格级别 。最后，有一个名为的级别 `EXPOSE_NOTHING`将不显示任何属性和方法。在这种情况下，您将能够通过散列模型接口访问的唯一数据是映射和资源包中的项目，以及从对泛型`get(Object)`或`get(String)` 方法的调用返回的对象 - 前提是受影响的对象具有此类方法。

## TemplateScalarModel功能

`java.lang.String`对象的模型将实现`TemplateScalarModel`其 `getAsString()`方法简单地委托给 `toString()`。需要注意的是包装`String`物进入包装豆提供了更多的功能不仅仅是他们是标量：因为散列上述接口，该模型包装 `String`S还可以访问所有 `String`的方法（`indexOf`， `substring`等），但他们大多具有天然的FreeMarker等效它们更好地使用（`s?index_of(n)`， `s[start..<end]`等）。

## TemplateNumberModel功能

作为`java.lang.Number`实现 实例的对象的模型包装器， `TemplateNumberModel`其 `getAsNumber()`方法返回包装的数字对象。请注意，将`Number`对象包装到Bean包装器中提供的功能远远多于它们作为数字模型：由于上面描述的哈希接口，包装`Number`s 的模型还提供对其所有方法的访问。

## TemplateCollectionModel功能

本机Java数组的模型包装器和所有实现的类`java.util.Collection`将实现 `TemplateCollectionModel`，从而获得通过`list`指令可用的附加功能 。

## TemplateSequenceModel功能

本机Java数组的模型包装器和实现的所有类都`java.util.List`将实现 `TemplateSequenceModel`，因此可以使用`model[i]` 语法通过索引访问它们的元素。您还可以使用`model?size`内置查询数组的长度或列表的大小。

此外，每一个接受一个参数是通过由反射方法调用分配的方法 `java.lang.Integer`（这些是 `int`，`long`， `float`，`double`，`java.lang.Object`， `java.lang.Number`，和 `java.lang.Integer`）也实现了这个接口。这意味着您可以方便地访问所谓的索引bean属性： `model.foo[i]`将转换为 `obj.getFoo(i)`。

## TemplateMethodModel功能

对象的所有方法都表示为 `TemplateMethodModelEx`可使用其对象模型上具有方法名称的哈希键访问的对象。当您使用参数调用方法时 ，将该方法作为模板模型传递给方法。该方法将首先尝试打开它们 - 有关展开的详细信息，请参阅下文。然后将这些展开的参数用于实际的方法调用。如果方法重载，将使用Java编译器用于从多个重载方法中选择方法的相同规则来选择最具体的方法。如果没有方法签名与传递的参数匹配，或者没有方法可以选择没有歧义， 则抛出a。`model.*method*(*arg1*, *arg2*, *...*)``TemplateModelException`

返回类型`void`返回的 方法`TemplateModel.NOTHING`，因此可以使用`${obj.method(args)}` 语法安全地调用它们。

实例的模型`java.util.Map`也实现`TemplateMethodModelEx`为调用其`get()`方法的手段。如前所述，您也可以使用哈希功能来访问“get”方法，但它有几个缺点：它的速度较慢，因为检查了第一个属性和方法名称的密钥; 与属性和方法名称冲突的键将被它们遮蔽; 最后，您只能使用`String`该方法使用密钥。相反，调用`model(key)` 转换为`model.get(key)`直接：它更快，因为没有属性和方法名称查找; 它不受影响; 最后它适用于非String键，因为参数是像普通方法调用一样解包的。实际上，`model(key)`a `Map` 等于`model.get(key)`，只写更短。

模型`java.util.ResourceBundle`也可以`TemplateMethodModelEx`作为资源访问和消息格式化的便捷方式实现。对bundle的单参数调用将检索具有与`toString()`unwrapped参数的值对应的名称的资源。对bundle的多参数调用也将检索具有与`toString()`unwrapped参数的值对应的名称的资源 ，但是它将使用它作为格式模式并将其传递给 `java.text.MessageFormat`使用第二个和后面的参数的展开值作为格式化参数。 `MessageFormat`对象将使用发起它们的bundle的语言环境进行初始化。

## 解开规则

从模板调用Java方法时，需要将其参数从模板模型转换回Java对象。假设目标类型（方法的形式参数的声明类型）表示为`T`，则按以下顺序尝试以下规则：

- 如果模型是此包装器的null模型，`null`则返回Java 。
- 如果模型实现 `AdapterTemplateModel`，`model.getAdaptedObject(T)`则返回结果， 如果它是实例`T`或是数字，并且可以`T`使用数字强制转换为如下所述的三个项目符号。BeansWrapper创建的所有模型本身都是AdapterTemplateModel实现，因此解包由BeansWrapper为底层Java对象创建的模型始终会生成原始Java对象。
- 如果模型实现了deprecated `WrapperTemplateModel`，`model.getWrappedObject()`则返回结果， 如果它是实例`T`或者是数字，并且可以`T`使用数字强制转换为如下所述的两个项目符号。
- 如果`T`是 `java.lang.String`，则`TemplateScalarModel`返回如果model实现 其字符串值。请注意，如果模型未实现TemplateScalarModel，我们不会尝试使用String.valueOf（model）自动将模型转换为字符串。您必须明确使用内置的？字符串将非标量作为字符串传递。
- 如果`T`是原始数字类型或 `java.lang.Number`可从`T`，并且模型实现 可分配 `TemplateNumberModel`，则如果它是实例`T`或其拳击类型（如果`T`是基本类型），则返回其数值。否则，如果`T`是内置Java数字类型（基本类型或标准子类 `java.lang.Number`，包括`BigInteger`和`BigDecimal`），`T`则使用数字模型的适当强制值创建类的新对象或其装箱类型。
- 如果`T`是`boolean`或 `java.lang.Boolean`，并且模型实现 `TemplateBooleanModel`，则返回其布尔值。
- 如果`T`是 `java.util.Map`和模型实现 `TemplateHashModel`，则返回散列模型的特殊Map表示。
- 如果`T`是 `java.util.List`和模型实现 `TemplateSequenceModel`，则返回序列模型的特殊List表示。
- 如果`T`是 `java.util.Set`和模型实现 `TemplateCollectionModel`，则返回集合模型的特殊Set表示。
- 如果`T`是 `java.util.Collection`或者 `java.lang.Iterable`模型实现了`TemplateCollectionModel`或者`TemplateSequenceModel`，则返回集合或序列模型的特殊Set或List表示。
- 如果`T`是Java数组类型并且模型实现`TemplateSequenceModel`，则创建指定类型的新数组，并使用数组的组件类型将其元素以递归方式展开到数组中`T`。
- 如果`T`是`char`或 `java.lang.Character`，并且模型实现 `TemplateScalarModel`，并且其字符串表示只包含一个字符，则返回`java.lang.Character`具有该值的a 。
- 如果`java.util.Date`可以从`T`，和模型实现 分配 `TemplateDateModel`，并且其日期值是实例`T`，则返回其日期值。
- 如果model是数字模型，并且其数值是实例`T`，则返回数值。 您可以拥有实现自定义接口的java.lang.Number的自定义子类，而T可能是该接口。（*）
- 如果model是日期模型，并且其日期值是实例`T`，则返回日期值。与（*）类似的考虑
- 如果model是标量模型，并且`T`可以从中分配`java.lang.String`，则返回字符串值。这包括T是java.lang.Object，java.lang.Comparable和java.io.Serializable（**）的情况
- 如果model是布尔模型，并且`T`可以从中`java.lang.Boolean`赋值，则返回布尔值。与...一样 （**）
- 如果model是散列模型，并且`T`可以从中分配 `freemarker.ext.beans.HashAdapter`，则返回散列适配器。与...一样 （**）
- 如果model是序列模型，并且`T`可以从中分配 `freemarker.ext.beans.SequenceAdapter`，则返回序列适配器。与...一样 （**）
- 如果model是集合模型，并且`T` 可以从中分配 `freemarker.ext.beans.SetAdapter`，则返回集合的set适配器。与...一样 （**）
- 如果模型是实例`T`，则返回模型本身。这包括方法显式声明FreeMarker特定模型接口的情况，以及在请求java.lang.Object时允许返回指令，方法和转换模型的情况。
- 抛出表示无法进行转换的异常。

## 访问静态方法

在`TemplateHashModel`从返回 `BeansWrapper.getStaticModels()`可用于访问静态方法和任意类的字段创建散列模型。

```
BeansWrapper wrapper = BeansWrapper.getDefaultInstance（）;
TemplateHashModel staticModels = wrapper.getStaticModels（）;
TemplateHashModel fileStatics =
    （TemplateHashModel）staticModels.get（“java.io.File”）;
```

并且您将获得一个模板哈希模型，该模型将类的所有静态方法和静态字段（包括final和非final）公开 `java.lang.System`为哈希键。假设您将以前的模型放在根模型中：

```
root.put（“File”，fileStatics）;
```

从现在开始，您可以使用`${File.SEPARATOR}` 将文件分隔符插入到模板中，或者甚至可以通过以下方式列出文件系统的所有根：

```
<#list File.listRoots（）as fileSystemRoot> ... </＃list>
```

当然，您必须意识到此模型带来的潜在安全问题。

您甚至可以通过将静态模型哈希放入模板根模型中，让模板作者完全自由地使用哪些类的静态方法

```
root.put（“statics”，BeansWrapper.getDefaultInstance（）。getStaticModels（））;
```

如果它被用作具有类名作为键的哈希，则此对象公开任何类的静态方法。然后，您可以像`${statics["java.lang.System"].currentTimeMillis()}` 在模板中一样使用表达式 。但请注意，这具有更多的安全隐患，因为`System.exit()`如果方法暴露级别被削弱，有人甚至可以使用此模型调用 `EXPOSE_ALL`。

请注意，在上面的示例中，我们始终使用默认 `BeansWrapper`实例。这是一个方便的静态包装器实例，您可以在大多数情况下使用它。您也可以自由地创建自己的`BeansWrapper`实例并使用它们，尤其是当您想要修改其某些特征（如模型缓存，安全级别或空模型表示）时。

## 访问枚举

在`TemplateHashModel`从返回 `BeansWrapper.getEnumModels()`可用于对JRE 1.5或更高版本的访问枚举的值来创建散列模型。（尝试在早期的JRE上调用此方法将导致 `UnsupportedOperationException`。）

```
BeansWrapper wrapper = BeansWrapper.getDefaultInstance（）;
TemplateHashModel enumModels = wrapper.getEnumModels（）;
TemplateHashModel roundingModeEnums =
    （TemplateHashModel）enumModels.get（“java.math.RoundingMode”）;
```

您将获得一个模板哈希模型，该模型将类的所有枚举值公开`java.math.RoundingMode`为哈希键。假设您将以前的模型放在根模型中：

```
root.put（“RoundingMode”，roundingModeEnums）;
```

从现在开始，您可以使用`RoundingMode.UP`表达式来引用`UP`模板中的枚举值。

您甚至可以通过将枚举模型哈希放入模板根模型中，让模板作者完全自由地使用它们使用的枚举类

```
root.put（“enums”，BeansWrapper.getDefaultInstance（）。getEnumModels（））;
```

如果将此类对象用作具有类名作为键的哈希，则此对象公开任何枚举类。然后，您可以像`${enums["java.math.RoundingMode"].UP}`在模板中一样使用表达式 。

暴露的枚举值可以用作标量（它们将委托给它们的`toString()`方法），也可以用于相等和不等式比较。

请注意，在上面的示例中，我们始终使用默认 `BeansWrapper`实例。这是一个方便的静态包装器实例，您可以在大多数情况下使用它。您也可以自由地创建自己的`BeansWrapper`实例并使用它们，尤其是当您想要修改其某些特征（如模型缓存，安全级别或空模型表示）时。