# autoesc

页面内容

- [概要](https://freemarker.apache.org/docs/ref_directive_autoesc.html#autoid_80)
- [描述](https://freemarker.apache.org/docs/ref_directive_autoesc.html#autoid_81)





## 概要

```
<#autoesc>
  ...
</#autoesc>
```

骆驼案名称变体： `autoEsc`

## 描述

打开嵌套部分中的[自动转义](https://freemarker.apache.org/docs/dgui_misc_autoescaping.html)。如果默认情况下当前[输出格式](https://freemarker.apache.org/docs/dgui_misc_autoescaping.html#dgui_misc_autoescaping_outputformat)具有自动转义功能，则默认情况下通常会启用 自动转义，因此您很少需要此功能。请注意，为了逃避禁用自动转义的单个 ，您应该使用 。`${*expression*}``${*expression*?esc}`

该指令仅对嵌套块内部的字面（如文本编辑器中）部分有效，而不是对从那里调用/包含的部分起作用。

例：

```
<#ftl output_format =“XML”auto_esc = false>
$ { “和”}
<#autoesc>
  $ { “和”}
  ...
  $ { “和”}
</＃autoesc>
$ { “和”}
＆
  ＆安培;
  ...
  ＆安培;
＆
```

`autoesc`不能使用在当前 [的输出格式](https://freemarker.apache.org/docs/dgui_misc_autoescaping.html#dgui_misc_autoescaping_outputformat)是一[非标记输出格式](https://freemarker.apache.org/docs/dgui_misc_autoescaping.html#dgui_misc_autoescaping_nonmarkupof)（因此不能做到逸出）。这样做是一个[解析时错误](https://freemarker.apache.org/docs/gloss.html#gloss.parseTimeError)。

`autoesc`也可以使用嵌套到[`noautoesc` 指令](https://freemarker.apache.org/docs/ref_directive_noautoesc.html)中重新启用自动转义。

`autoesc`可用于已启用自动转义的位置，例如甚至在另一个 `autoesc`块内。这样做是多余的，但允许。