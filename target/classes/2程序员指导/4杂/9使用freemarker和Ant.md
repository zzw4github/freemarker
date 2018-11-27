# 使用FreeMarker和Ant

我们知道有两个“FreeMarker Ant任务”：

- `FreemarkerXmlTask`：它附带FreeMarker发行版，打包成 `freemarker.jar`。这是一个轻量级，易于使用的Ant任务，用于使用FreeMarker模板转换XML文档。它的方法是源文件（输入文件）是XML文件，由单个模板呈现给相应的输出文件。也就是说，对于每个XML文件，将执行模板（使用数据模型中的XML文档），并且模板输出将被写入名称与XML文件名称相似的文件中。因此，模板文件扮演与XSLT样式表类似的角色，但它是FTL，而不是XSLT。
- FMPP：它是一个更重量级，更少以XML为中心的第三方Ant任务（以及独立的命令行工具）。它的主要方法是源文件（输入文件）是模板文件，它们自己生成相应的输出文件，但它也支持`FreemarkerXmlTask`XML-s源文件的方法 。此外，它还具有额外的功能`FreemarkerXmlTask`。那有什么缺点呢？由于它更复杂，更通用，因此更难学习和使用它。

本节介绍 `FreemarkerXmlTask`。有关FMPP的更多信息，请访问其主页：[http](http://fmpp.sourceforge.net/)：//fmpp.sourceforge.net/ 。

要使用`FreemarkerXmlTask`，必须首先`freemarker.ext.ant.FreemarkerXmlTask`在Ant构建文件中定义 ，然后调用该任务。假设您想使用假设的“xml2html.ftl”模板将多个XML文档转换为HTML，XML文档位于“xml”目录中，HTML文档生成目录“html”。你会写类似的东西：

```
<taskdef name =“freemarker”classname =“freemarker.ext.ant.FreemarkerXmlTask”>
  <类路径>
    <pathelement location =“freemarker.jar”/>
  </类路径>
</的taskdef>
<mkdir dir =“html”/>
<freemarker basedir =“xml”destdir =“html”includes =“** / * .xml”template =“xml2html.ftl”/>
```

该任务将为每个XML文档调用模板。每个文档都将被解析为DOM树，然后包装为FreeMarker节点变量。模板处理开始时，特殊变量,, `.node`设置为XML文档的根节点。

请注意，如果您使用的是旧版（FreeMarker 2.2.x及更早版本）XML适配器实现，那么它仍然有效，并且XML树的根将作为变量放在数据模型中 `document`。它包含遗留`freemarker.ext.xml.NodeListModel`类的一个实例 。

请注意，构建文件定义的所有属性都将作为名为“properties”的哈希模型提供。其他几种型号可供选择; 有关哪些变量可用于模板以及该任务可以接受的其他属性的详细说明，请参阅JavaDoc for `freemarker.ext.ant.FreemarkerXmlTask`。