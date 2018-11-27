# outputformat

页面内容

- [概要](https://freemarker.apache.org/docs/ref_directive_outputformat.html#autoid_114)
- [描述](https://freemarker.apache.org/docs/ref_directive_outputformat.html#autoid_115)





## 概要

```
<#outputformat formatName>
  ...
</#outputFormat>
```

哪里：

- `*formatName*`：一个字符串常量; 不能包含运行时表达式！这是输出格式的，如名称`"HTML"`， `"XML"`等; 请参阅[此处预定义输出格式](https://freemarker.apache.org/docs/dgui_misc_autoescaping.html#topic.predefinedOutputFormats)的[表格](https://freemarker.apache.org/docs/dgui_misc_autoescaping.html#topic.predefinedOutputFormats)。必须知道引用的输出格式`Configuration`，否则将发生[分析时错误](https://freemarker.apache.org/docs/gloss.html#gloss.parseTimeError)。名称也可以是 ，或 ; [稍后查看组合输出格式](https://freemarker.apache.org/docs/ref_directive_outputformat.html#topic.combinedOutputFormats)。`"*outerFormatName*{*innerFormatName*}"``"{*innerFormatName*}"`

骆驼案名称变体： `outputFormat`

注意：

`outputformat` 从FreeMarker 2.3.24开始存在。



## 描述

在嵌套块内将[输出格式](https://freemarker.apache.org/docs/dgui_misc_autoescaping.html#dgui_misc_autoescaping_outputformat)设置为指定的[格式](https://freemarker.apache.org/docs/dgui_misc_autoescaping.html#dgui_misc_autoescaping_outputformat)。在块结束时，将恢复较早的输出格式。

该指令仅对嵌套块内部的字面（如文本编辑器中）部分有效，而不是对从那里调用/包含的部分起作用。

例：

```
<#ftl output_format =“XML”> 
XML转义：$ {“＆{}”} 
<#outputformat“RTF”> 
  RTF转义：$ {“＆{}”} 
</＃outputformat> 
<#outputformat“plainText”> 
  没有escsaping：$ {“＆{}”} 
</＃outputformat> 
XML escsaping：$ {“＆{}”}
XML escsaping：＆amp; {} 
  RTF转义：＆\ {\} 
  没有escsaping：＆{} 
XML escsaping：＆amp; {}
```

### 组合（嵌套）输出格式

当`outputformat`-s相互嵌套时，通常只计算最里面的输出格式。例如：

```
<#ftl output_format =“XML”> 
$ {“'{}”} 
<#outputformat“HTML”> 
  $ {“'{}”} 
  <#outputformat“RTF”> 
    $ {“'{}”} 
  </＃ outputformat> 
</＃outputformat>
'{} 
  ＆＃39; {} 
    '\ {\}
```

但有时您希望一次性应用所有封闭的输出格式转义。在这种情况下，上面的第二个 应该使用，然后使用 ，以及第三个 应该使用然后使用 ，然后使用。这些被称为组合输出格式，并且可以分别由诸如和 的名称引用。我们可以在之前的两个 调用中使用这些名称，但是，有一个简写，您可以 从封闭的输出格式继承该部分：`${*...*}``"HTML"``"XML"``${*...*}``"RTF"``"HTML"``"XML"``"XML{HTML}"``"XML{HTLM{RTF}}"``outputformat``{*...*}`

```
<#ftl outputFormat =“XML”> 
$ {“'{}”} 
<#outputFormat“{HTML}”> <＃ - 与“XML {HTML}”相同 - > 
  $ {“'{}”} 
  < #outputFormat'{RTF}'> <＃ - 与“XML {HTML {RTF}}”相同 - > 
    $ {“'{}”} 
  </＃outputFormat> 
</＃outputFormat>
'{} 
  ＆amp;＃39; {} 
    ＆amp;＃39; \ {\}
```