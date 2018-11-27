# 传统的XML包装器实现

页面内容

- [TemplateScalarModel](https://freemarker.apache.org/docs/pgui_misc_xml_legacy.html#autoid_65)
- [TemplateCollectionModel](https://freemarker.apache.org/docs/pgui_misc_xml_legacy.html#autoid_66)
- [TemplateSequenceModel](https://freemarker.apache.org/docs/pgui_misc_xml_legacy.html#autoid_67)
- [TemplateHashModel](https://freemarker.apache.org/docs/pgui_misc_xml_legacy.html#autoid_68)
- [TemplateMethodModel](https://freemarker.apache.org/docs/pgui_misc_xml_legacy.html#autoid_69)
- [命名空间处理](https://freemarker.apache.org/docs/pgui_misc_xml_legacy.html#autoid_70)

注意：

*不推荐使用旧的XML包装器。* FreeMarker 2.3引入了对新XML处理模型的支持。为了支持这一点，我们引入了一个新的XML包装器包 `freemarker.ext.dom`。对于新用法，我们建议您使用它。它在[XML处理指南](https://freemarker.apache.org/docs/xgui.html)部分中有记录。

该类`freemarker.ext.xml.NodeListModel` 提供了一个模板模型，用于包装表示为节点树的XML文档。每个节点列表可以包含零个或多个XML节点（文档，元素，文本，处理指令，注释，实体引用，CDATA部分等）。节点列表使用以下语义实现以下模板模型接口：

## TemplateScalarModel

当用作标量时，节点列表将呈现表示其包含的节点的XML片段。这使得它可以方便地用于XML到XML的转换模板。

## TemplateCollectionModel

当用作带`list` 指令的集合时，它将简单地枚举其节点。每个节点都将作为由单个节点组成的新节点列表返回。

## TemplateSequenceModel

当用作序列时，它将第i个节点作为由单个请求节点组成的新节点列表返回。即返回`<chapter>`元素的第3个元素 `<book>`，您将使用以下（注意索引从零开始）：

```
<#assign thirdChapter = xmldoc.book.chapter [2]>
```

## TemplateHashModel

当用作哈希时，它基本上用于遍历子项。也就是说，如果您有一个名为 `book`包含多个章节的元素节点`book.chapter`的节点列表，那么将产生一个节点列表，其中包含该book元素的所有章节元素。at符号用于引用属性：`book.@title`产生具有单个属性节点的节点列表，即book元素的title属性。

实现的后果，例如，如果是重要的`book`没有`chapter`-s则 `book.chapter`是一个空序列，所以 `xmldoc.book.chapter??`将 *不会*是`false`，这将是永远`true`！同样， `xmldoc.book.somethingTotallyNonsense??`也不会`false`。要检查是否找不到孩子，请使用`xmldoc.book.chapter?size == 0`。

哈希定义了几个“魔术键”。所有这些键都以下划线开头。最值得注意的是`_text`检索节点文本的 键： `${book.@title._text}`将属性的值呈现到模板中。同样，`_name` 将检索元素或属性的名称。 `*`或`_allChildren`返回节点列表中所有元素的所有直接子元素，同时 `@*`或`_allAttributes`返回节点列表中元素的所有属性。还有更多这样的钥匙; 这里是所有哈希键的详细摘要：

| 关键名称                          | 评估到                                                       |
| --------------------------------- | ------------------------------------------------------------ |
| `*` 要么`_children`               | 当前节点的所有直接元素子节点（非递归）。适用于元素和文档节点。 |
| `@*` 要么`_attributes`            | 当前节点的所有属性。仅适用于元素。                           |
| `@*attributeName*`                | 当前节点的命名属性。适用于元素，文档类型和处理说明。在doctypes上它支持属性`publicId`， `systemId`和 `elementName`。在处理指令上，它支持属性`target`以及 `data`数据中指定的任何其他属性名称作为`name="value"`对。doctype和处理指令的属性节点是合成的，因此没有父节点。但请注意，这 `@*`不适用于doctypes或处理指令。 |
| `_ancestor`                       | 所有祖先直到当前节点的根元素（递归）。适用于相同的节点类型 `_parent`。 |
| `_ancestorOrSelf`                 | 当前节点的所有祖先加上当前节点。适用于相同的节点类型 `_parent`。 |
| `_cname`                          | 当前节点的规范名称（名称空间URI +本地名称），每个节点一个字符串（非递归）。适用于元素和属性 |
| `_content`                        | 当前节点的完整内容，包括子元素，文本，实体引用和处理指令（非递归）。适用于元素和文档。 |
| `_descendant`                     | 当前节点的所有递归后代元素子节点。适用于文档和元素节点。     |
| `_descendantOrSelf`               | 当前节点加当前节点的所有递归后代元素子节点。适用于文档和元素节点。 |
| `_document`                       | 当前节点所属的所有文档。适用于除文本以外的所有节点。         |
| `_doctype`                        | 当前节点的doctypes。仅适用于文档节点。                       |
| `_filterType`                     | 是一种按类型过滤的模板方法模型。调用时，它将生成一个节点列表，该列表仅包含那些类型与作为参数传递的类型之一匹配的当前节点。您应该将任意数量的字符串传递给此方法，该方法包含要保留的类型的名称。有效的类型名称是：“attribute”，“cdata”，“comment”，“document”，“documentType”，“element”，“entity”，“entityReference”，“processingInstruction”，“text”。 |
| `_name`                           | 当前节点的名称，每个节点一个字符串（非递归）。适用于元素和属性（返回其本地名称），实体，处理指令（返回其目标），doctypes（返回其公共ID） |
| `_nsprefix`                       | 当前节点的名称空间前缀，每个节点一个字符串（非递归）。适用于元素和属性 |
| `_nsuri`                          | 当前节点的名称空间URI，每个节点一个字符串（非递归）。适用于元素和属性 |
| `_parent`                         | 当前节点的父元素。适用于元素，属性，注释，实体，处理指令。   |
| `_qname`                          | `[namespacePrefix:]localName`表单中当前节点的限定名称， 每个节点一个字符串（非递归）。适用于元素和属性 |
| `_registerNamespace(prefix, uri)` | 使用当前节点列表的指定前缀和URI以及从当前节点列表派生的所有节点列表注册XML名称空间。注册后，您可以使用`nodelist["prefix:localname"]`或 `nodelist["@prefix:localname"]`语法来访问名称为命名空间范围的元素和属性。请注意，名称空间前缀不需要与XML文档本身使用的实际前缀匹配，因为名称空间仅通过其URI进行比较。 |
| `_text`                           | 当前节点的文本，每个节点一个字符串（非递归）。适用于元素，属性，注释，处理指令（返回其数据）和CDATA部分。保留的XML字符（'<'和'＆'）不会被转义。 |
| `_type`                           | 返回一个节点列表，每个节点包含一个描述节点类型的字符串。可能的节点类型名称是：“attribute”，“cdata”，“comment”，“document”，“documentType”，“element”，“entity”，“entityReference”，“processingInstruction”，“text”。如果节点的类型未知，则返回“unknown”。 |
| `_unique`                         | 当前节点的副本，仅保留每个节点的第一个匹配项，从而消除重复项。通过应用uptree-traversals `_parent`，`_ancestor`和 `_ancestorOrSelf`， 可以在节点列表中出现重复项 `_document`。即 `foo._children._parent`返回一个节点列表，该节点列表在foo中具有重复的节点 - 每个节点的出现次数等于其子节点的数量。在这些情况下，用于 `foo._children._parent._unique`消除重复。适用于所有节点类型。 |
| 任何其他钥匙                      | 元素名称与键匹配的当前节点的子元素。这样可以方便地在`book.chapter.title`样式语法中进行子遍历 。请注意，这 `nodeset.childname`在技术上等同于`nodeset("childname")`，但写入和评估速度更快。适用于文档和元素节点。 |

## TemplateMethodModel

当用作方法模型时，它返回一个节点列表，该列表是在节点列表的当前内容上评估XPath表达式的结果。要使此功能起作用，必须`Jaxen`在类路径中包含 库。例如：

```
<#assign firstChapter = xmldoc（“// chapter [first（）]”）>
```

## 命名空间处理

为了遍历具有命名空间范围名称的子元素，可以使用节点列表注册名称空间前缀。你可以用Java来做，调用

```
public void registerNamespace（String prefix，String uri）;
```

方法，或在模板内使用

```
$ { nodelist ._registerNamespace（prefix，uri）}
```

句法。从那以后，您可以通过语法引用由特定URI表示的命名空间中的子元素

```
nodelist["prefix:localName"]
```

和

```
nodelist["@prefix:localName"]
```

以及在XPath表达式中使用这些名称空间前缀。向节点列表注册的命名空间将传播到从原始节点列表派生的所有节点列表。另请注意，名称空间仅与其URI匹配，因此您可以安全地为模板中的名称空间使用前缀，该名称空间与实际XML文档中的前缀不同 - 前缀只是模板中URI的本地别名。在XML文档中。