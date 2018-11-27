# noparse

页面内容

- [概要](https://freemarker.apache.org/docs/ref_directive_noparse.html#autoid_110)
- [描述](https://freemarker.apache.org/docs/ref_directive_noparse.html#autoid_111)





## 概要

```
<#noparse>
  ...
</#noparse>
```

骆驼案名称变体： `noParse`

## 描述

除了noparse end-tag之外，FreeMarker不会在该指令的主体中搜索FTL标签和插值以及其他特殊字符序列。

例：

```
例：
--------

<#noparse>
  <#list动物作为动物>
  <tr> <td> $ {animal.name} <td> $ {animal.price}欧元
  </＃列表>
</＃noparse>
例：
--------

  <#list动物作为动物>
  <tr> <td> $ {animal.name} <td> $ {animal.price}欧元
  </＃列表>
```