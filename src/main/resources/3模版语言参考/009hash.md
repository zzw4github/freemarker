# 哈希内置插件

页面内容

- [key](https://freemarker.apache.org/docs/ref_builtins_hash.html#ref_builtin_keys)
- [value](https://freemarker.apache.org/docs/ref_builtins_hash.html#ref_builtin_values)

## key

包含散列中所有查找键的序列。

```
<#assign myHash = {“name”：“mouse”，“price”：50}>
<#list myHash？键为k>
  $ {K}
</＃列表>
  名称
  价钱
```

请注意，并非所有哈希都支持此功能（询问程序员是否允许使用某个哈希值）。

由于哈希一般不定义其子变量的顺序，因此返回键名的顺序可以是任意的。但是，一些哈希值保持有意义的顺序（询问程序员是否有某个哈希值）。例如，使用上述 语法创建的哈希保留与指定子变量相同的顺序。`{*...*}`

注意：

要列出键和值，您可以使用 `<#list attrs as key, value>...<#list>`; 看到[`list` 指令](https://freemarker.apache.org/docs/ref_directive_list.html#ref.directive.list)。

## value

包含散列中所有变量（键值对中的值）的序列。

```
<#assign myHash = {“name”：“mouse”，“price”：50}>
<#list myHash？值为v>
  $ {V}
</＃列表>
  老鼠
  50
```

请注意，并非所有哈希都支持此功能（询问程序员是否允许使用某个哈希值）。

从返回值的顺序来看，同样适用于`keys`内置; 看那边。此外，不保证值的顺序对应于`keys`内置返回的键的顺序 。

注意：

要列出键和值，您可以使用 `<#list attrs as key, value>...<#list>`; 看到[`list` 指令](https://freemarker.apache.org/docs/ref_directive_list.html#ref.directive.list)。