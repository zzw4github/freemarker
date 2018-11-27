# Jython包装器

页面内容

- [TemplateHashModel功能](https://freemarker.apache.org/docs/pgui_misc_jythonwrapper.html#autoid_71)
- [TemplateScalarModel功能](https://freemarker.apache.org/docs/pgui_misc_jythonwrapper.html#autoid_72)
- [TemplateBooleanModel功能](https://freemarker.apache.org/docs/pgui_misc_jythonwrapper.html#autoid_73)
- [TemplateNumberModel功能](https://freemarker.apache.org/docs/pgui_misc_jythonwrapper.html#autoid_74)
- [TemplateSequenceModel功能](https://freemarker.apache.org/docs/pgui_misc_jythonwrapper.html#autoid_75)

该`freemarker.ext.jython`软件包包含可以将任何Jython对象用作的模型 `TemplateModel`。在最基本的情况下，你只需要打电话给

```
public TemplateModel wrap（Object obj）;
freemarker.ext.jython.JythonWrapper`班级的方法 。此方法将传递的对象包装为适当的对象 `TemplateModel`。下面是返回的模型包装器的属性摘要。假设为了下面的讨论，在模板模型根中命名由`JythonWrapper`对象调用 产生的模型。`obj``model
```

## TemplateHashModel功能

`PyDictionary`并且 `PyStringMap`将被包装成一个散列模式。密钥查找映射到`__finditem__` 方法; 如果找不到项目，`None` 则返回模型。

## TemplateScalarModel功能

每个python对象都将实现 `TemplateScalarModel`其 `getAsString()`方法简单地委托给它 `toString()`。

## TemplateBooleanModel功能

每个python对象都将实现 `TemplateBooleanModel`其`getAsBoolean()`方法`__nonzero__()`根据Python语义true / false 简单地委托给 它 。

## TemplateNumberModel功能

用于`PyInteger`， `PyLong`和`PyFloat`对象的模型包装器实现`TemplateNumberModel`其 `getAsNumber()`方法返回`__tojava__(java.lang.Number.class)`。

## TemplateSequenceModel功能

扩展的所有类的模型包装器 `PySequence`将实现 `TemplateSequenceModel`，因此它们的元素将通过索引使用`model[i]` 将委托的语法访问`__finditem__(i)`。您还可以使用`model?size`将委派给的内置函数查询数组的长度或列表的大小`__len__()`。