# noautoesc

页面内容

- [概要](https://freemarker.apache.org/docs/ref_directive_noautoesc.html#autoid_108)
- [描述](https://freemarker.apache.org/docs/ref_directive_noautoesc.html#autoid_109)





## 概要

```
<#noautoesc>
  ...
</#noautoesc>
```

骆驼案名称变体： `noAutoEsc`

## 描述



禁用嵌套部分中的[自动转义](https://freemarker.apache.org/docs/dgui_misc_autoescaping.html)。请注意，为了防止仅为单个转义， 您应该使用 。`${*expression*}``${*expression*?no_esc}`

该指令仅对嵌套块内部的字面（如文本编辑器中）部分有效，而不是对从那里调用/包含的部分起作用。

例：

```
<#ftl output_format =“XML”>
$ { “和”}
<#noautoesc>
  $ { “和”}
  ...
  $ { “和”}
</＃noautoesc>
$ { “和”}
＆安培;
  ＆
  ...
  ＆
＆安培;
```

`noautoesc`无论当前的[输出格式](https://freemarker.apache.org/docs/dgui_misc_autoescaping.html#dgui_misc_autoescaping_outputformat) 是什么，都可以使用（与[`autoesc` 指令](https://freemarker.apache.org/docs/ref_directive_autoesc.html)不同）。

`noautoesc`也可以使用嵌套到 [`autoesc` 指令](https://freemarker.apache.org/docs/ref_directive_autoesc.html)中重新启用转义。

`noautoesc`可用于已禁用自动转义的位置，例如甚至在另一个 `noautoesc`块内。这样做是多余的，但允许。