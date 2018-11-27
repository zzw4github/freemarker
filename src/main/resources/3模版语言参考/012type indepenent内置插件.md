# Type independent built-ins

这些内置插件并不关心（左）左手参数的类型。

## switch

注意：

自FreeMarker 2.3.23以来，这种内置存在。

这基本上是[`switch`- `case`- `default` 指令](https://freemarker.apache.org/docs/ref_directive_switch.html)的内联（表达式）版本 。它的通用格式如下 ， 可以省略。例：`*matchedValue*?switch(*case1*,*result1*, *case2*, *result2*, ... *caseN*, *resultN*, *defaultResult*)``*defaultResult*`

```
<#list ['r'，'w'，'x'，'s']作为标志> 
  $ {flag ？switch（'r'，'可读'，'w''可写'，'x'，'可执行文件'，'未知标志：'+ flag） } 
</＃list>
  可读的
  可写
  可执行文件
  未知标志：s
```

也就是说，`switch`将找到`*case*`其值等于的第一个 参数（从左到右） `*matchedValue*`，然后它返回 `*result*`直接在该`*case*`参数之后的 参数的值。如果它没有找到相等的 `*case*`，那么它将返回该值 `*defaultResult*`，或者如果没有 `*defaultResult*` 参数（即，如果参数的数量是偶数），则它会停止模板处理并出错。

更多详情：

- `*matchedValue*`与`*case*`参数值的比较 [与`==` 操作符](https://freemarker.apache.org/docs/dgui_template_exp.html#dgui_template_exp_comparison)完全相同。因此，它只比较标量和只有相同类型的值。因此，类似的东西`x?switch(1, "r1", "c2", "r2")`没有意义，好像 `x`是非数字然后第一种情况会导致错误，如果`x`是数字，那么第二种情况将导致错误（除非`x`是 `1`，因为那时我们将不会做进一步的比较后第一）。
- 与普通方法调用不同，只 评估那些确实需要的参数。例如， 如果回报率 ，回报率 和回报 ，那么只有以下功能将被调用，并在此顺序：， ，， 。（当然，未评估的参数可以引用缺失的变量而不会导致错误。）它保证了 `switch(*...*)``two()?switch(c1(), r1(), c2(), r2(), c3(), r3())``two()``2``c1()``1``c2()``2``m()``c1()``c2()``r2()``*case*`参数表达式从左到右计算，并且只在找到第一个匹配项之前。它还保证只评估`*result*`属于第一个匹配的 表达式 `*case*`。它还保证 `*defaultResult*` 只有在没有匹配`*case*` 参数的情况下才会计算表达式 。
- 该`*case*` 参数表达式不必是恒定的值，也可以是任意复杂的表达式。当然，这同样适用于和`*result*`， `*defaultResult*`和`*matchedValue*`。
- 关于`*case*`参数值的类型没有限制 ，例如它们可以是字符串，数字或日期等。但是，由于`==`操作符的工作原理，`*case*`在*同一个* 内部使用不同类型的参数没有意义 `switch`（见前面的原因）。
- 与[`case` 指令](https://freemarker.apache.org/docs/ref_directive_switch.html)不同，那里没有直通行为，也就是说，不需要等效的 `break`指令。

注意：

如果你需要用布尔值切换，你应该使用 [`then` 内置的](https://freemarker.apache.org/docs/ref_builtins_boolean.html#ref_builtin_then)，比如 。`*matchedBoolean*?then(*whenTrue*, *whenFalse*)`

注意：

如果你需要在参数上进行任意逻辑测试而不是简单的相等比较 `*case*`，你可以做这样的事情（这里我们测试范围）： `true?switch(priority <= 1, "low", priority == 2, "medium", priority >= 3, "high")`