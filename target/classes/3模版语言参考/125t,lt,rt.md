# t，lt，rt

页面内容

- [概要](https://freemarker.apache.org/docs/ref_directive_t.html#autoid_122)
- [描述](https://freemarker.apache.org/docs/ref_directive_t.html#autoid_123)









## 概要

```
<#t>

<#lt>

<#rt>
```

## 描述

这些指令指示FreeMarker忽略标记行中的某些空格：

- `t` （用于修剪）：忽略此行中的所有前导和尾随空格。
- `lt` （对于左侧修剪）：忽略此行中的所有前导空格。
- `rt` （对于右侧修剪）：忽略此行中的所有尾随空格。

哪里：

- “前导空格”表示[在行](https://freemarker.apache.org/docs/gloss.html#gloss.lineBreak)的第一个非空白字符之前的所有空格和制表符（以及根据[UNICODE的](https://freemarker.apache.org/docs/gloss.html#gloss.unicode)其他字符，除了[换行符](https://freemarker.apache.org/docs/gloss.html#gloss.lineBreak)之外的空格）。
- “trailing white-space”表示在行的最后一个非空格字符之后的所有空格和制表符（以及根据[UNICODE](https://freemarker.apache.org/docs/gloss.html#gloss.unicode)为空格的其他字符，除了换行符），*以及*在行尾的换行符线。

重要的是要理解这些指令检查模板本身，而*不是*输出模板在与数据模型合并时生成的内容。 （也就是说，在解析时发生了空白区域删除。）

例如：

```
- 
  1 <#t>
  2 <#T>
  3 <#lt>
  4
  5 <#rt>
  6
- 
```

将输出：

```
- 
1 23
  4
  5 6
- 
```

将这些指令放在行内并不重要。也就是说，无论是将指令放在行的开头，行的末尾还是行的中间，效果都是相同的。