# visit, recurse, fallback



# 访问，递归，后备



页面内容

- [概要](https://freemarker.apache.org/docs/ref_directive_visit.html#autoid_126)
- 描述
  - [访问](https://freemarker.apache.org/docs/ref_directive_visit.html#autoid_128)
  - [递归](https://freemarker.apache.org/docs/ref_directive_visit.html#autoid_129)
  - [倒退](https://freemarker.apache.org/docs/ref_directive_visit.html#autoid_130)





## 概要

```
<#visit node using namespace>
要么
<#visit node>
<#recurse node using namespace>
要么
<#recurse node>
要么
<#recurse using namespace>
要么
<#recurse>
<#fallback>
```

哪里：

- `*node*`：Expression计算为[节点变量](https://freemarker.apache.org/docs/xgui_expose_dom.html)。
- `*namespace*`： [命名空间](https://freemarker.apache.org/docs/dgui_misc_namespace.html)或命名空间序列。命名空间可以使用命名空间哈希（也称为门哈希），或者使用字符串文字来存储可以导入的模板的路径。您也可以使用普通哈希值代替命名空间哈希值。

## 描述

在`visit`和`recurse` 指令用于树木的递归处理。实际上，这主要用于[处理XML。](https://freemarker.apache.org/docs/xgui.html)

### 访问

当您调用时，它会查找要从节点的name（）和namespace（）中扣除名称的用户定义的指令（如宏）。名称扣除规则：`<#visit*node*>``*node*?node_name``*node*?node_namesoace`

- 如果节点不支持节点名称空间（作为XML中的文本节点），则指令名称只是node（`*node*?node_name`）的名称。 如果`getNodeNamespace`方法返回，则节点不支持节点名称空间`null`。
- 如果节点确实支持节点名称空间（作为XML中的元素节点），则从节点名称空间推导出的前缀可以在节点名称之前附加，其中冒号用作分隔符（例如`e:book`）。前缀，如果有一个前缀，则取决于在[FTL命名空间](https://freemarker.apache.org/docs/dgui_misc_namespace.html)中`ns_prefixes` 使用`ftl`指令参数注册的前缀，其中 查找处理程序指令（与调用的FTL名称空间不一定相同） 从，你将在后面看到）。具体来说，如果没有注册默认命名空间`visit``visit``ns_prefixes`那么对于不属于任何命名空间节点（当`getNodeNamespace` 收益`""`）不使用前缀。如果使用了默认名称空间， `ns_prefixes`则使用不属于任何名称空间前缀`N`的节点，对于属于默认节点名称空间的节点，不使用前缀。否则，在这两种情况下，`ns_prefixes` 都使用与节点命名空间关联的前缀。如果没有与节点的节点名称空间关联的前缀，则`visit`只需表现为没有找到具有正确名称的指令。

调用用户定义的指令的节点可用作特殊变量`.node`。例：

```
<＃ - 假设nodeWithNameX？node_name是“x” - >
<#visit nodeWithNameX>
完成。
<#macro x>
   现在我正在处理一个名为“x”的节点。
   只是为了说明如何访问此节点：此节点具有$ {。node？children？size}子节点。
</＃宏>
```

输出将是这样的：

```
   现在我正在处理一个名为“x”的节点。
   只是为了说明如何访问此节点：此节点有3个子节点。
完成。
```

如果使用optional `using`子句指定了一个或多个名称空间 ，那么`visit` 将仅查找这些名称空间中的指令，并使列表中较早指定的名称空间获得优先级。如果未 `using`指定`using` 子句，`visit`则重用使用上次未完成调用的子句指定的命名空间或命名空间序列。如果没有此类挂起`visit`调用，则使用当前命名空间。例如，如果您执行此模板：

```
<#import“n1.ftl”as n1>
<#import“n2.ftl”为n2>

<＃ - 这将调用n2.x（因为没有n1.x）： - >
<#visit nodeWithNameX使用[n1，n2]>

<＃ - 这将调用当前命名空间的x： - >
<#visit nodeWithNameX>

<#macro x>
  只需x
</＃宏>
```

这是`n1.ftl`：

```
<#macro y>
  n1.y
</＃宏>
```

这是`n2.ftl`：

```
<#macro x>
  n2.x
  <＃ - 此callc n1.y继承了待处理访问调用中的“using [n1，n2]”： - >
  <#visit nodeWithNameY>
  <＃ - 这将调用n2.y： - >
  <#visit nodeWithNameY using .namespace>
</＃宏>

<#macro y>
  n2.y
</＃宏>
```

然后这将打印：

```
  n2.x
  n1.y
  n2.y

  只需x
 
```

如果`visit`在FTL命名空间中找不到用户定义的指令，其名称与前面描述的规则推导出的名称相同，那么它会尝试查找带有名称的用户定义指令 ，或者节点是否支持节点类型property（即 返回undefined变量），然后是name 。对于查找，它使用与前面解释的相同的机制。如果仍未找到用于处理节点的用户定义指令，则会 停止模板处理并显示错误。某些特定于XML的节点类型在这方面有特殊处理; 看到：`@*node_type*``*node*?node_type``@default``visit`[XML处理指南/声明性XML处理/详细信息](https://freemarker.apache.org/docs/xgui_declarative_details.html)。例：

```
<＃ - 假设nodeWithNameX？node_name是“x” - >
<#visit nodeWithNameX>

<＃ - 假设nodeWithNameY？node_type是“foo” - >
<#visit nodeWithNameY>

<#macro x>
处理节点x
</＃宏>

<#macro @foo>
节点$ {node？node_name}没有特定的处理程序
</＃宏>
```

这将打印：

```
处理节点x

节点y没有特定的处理程序

 
```

### 递归





该`<#recurse>`指令实际上是语法糖。它访问节点的所有子节点（而不是节点本身）。所以，写：

```
<#recurse someNode using someLib >
```

相当于写作：

```
<#list someNode？children as child > < ＃visit child using someLib > </＃list>
```

但是，目标节点在`recurse`指令中是可选的 。如果未指定目标节点，则只使用`.node`。因此，简洁的指令`<#recurse>`相当于：

```
<#list .node？children as children> <#visit child> </ #list>
```

作为熟悉XSLT的人的旁注，`<#recurse>`几乎与XSLT 中的`<xsl:apply-templates/>` 指令类似。

### 倒退





如前所述，在`visit`指令的文档中 ，可以在多个FTL名称空间中搜索处理节点的用户定义指令。该`fallback`指令可以在用于处理节点的用户定义指令中使用。它指示FreeMarker继续在更多名称空间中搜索用户定义的指令（即，在名称空间列表中当前调用的用户定义指令的名称空间之后的名称空间中） ）。如果找到该节点的处理程序，则调用它，否则 `fallback`不执行任何操作。

这是在处理程序库上编写自定义层的典型用法，有时会将处理传递给自定义库：

```
<#import "/lib/docbook.ftl" as docbook>

<#--
  We use the docbook library, but we override some handlers
  in this namespace.
-->
<#visit document using [.namespace, docbook]>

<#--
  Override the "programlisting" handler, but only in the case if
  its "role" attribute is "java"
-->
<#macro programlisting>
  <#if .node.@role[0]!"" == "java">
    <#-- Do something special here... -->
    ...
  <#else>
    <#-- Just use the original (overidden) handler -->
    <#fallback>
  </#if>
</#macro>
```