页面内容

- [概要](https://freemarker.apache.org/docs/ref_directive_compress.html#autoid_82)
- [描述](https://freemarker.apache.org/docs/ref_directive_compress.html#autoid_83)





## 概要

```
<#compress>
  ...
</#compress>
```

## 描述

当您使用白色空间不敏感格式（例如HTML或XML）时，compress指令对于删除多余的[空白区域](https://freemarker.apache.org/docs/gloss.html#gloss.whiteSpace)非常有用 。它捕获在其体内生成的输出（即在其开始标记和结束标记之间），并将所有未破碎的空白序列减少为单个空白字符。插入的字符将是一个[换行符](https://freemarker.apache.org/docs/gloss.html#gloss.lineBreak)，如果换成序列包含换行符，或空格，否则。将完全删除第一个也是最后一个完整的白色空间序列。

```
<#assign x =“moo \ n \ n”>
（<#compress>
  1 2 3 4 5
  $ {}哞
  仅测试

  我说，只测试

</＃压缩>）
```

将输出：

```
（1 2 3 4 5
哞
仅测试
我说，只测试）
```