# list, else, items, sep, break, continue

页面内容

- [概要](https://freemarker.apache.org/docs/ref_directive_list.html#autoid_100)
- 描述
  - [最简单的形式](https://freemarker.apache.org/docs/ref_directive_list.html#ref_list_simple)
  - [其他指令](https://freemarker.apache.org/docs/ref_directive_list.html#ref_list_else)
  - [items指令](https://freemarker.apache.org/docs/ref_directive_list.html#ref_list_items)
  - [sep指令](https://freemarker.apache.org/docs/ref_directive_list.html#ref_list_sep)
  - [break指令](https://freemarker.apache.org/docs/ref_directive_list.html#ref_list_break)
  - [continue指示](https://freemarker.apache.org/docs/ref_directive_list.html#ref_list_continue)
  - [accessing iteration状态](https://freemarker.apache.org/docs/ref_directive_list.html#ref_list_accessing_state)
  - [nestiing loops 彼此](https://freemarker.apache.org/docs/ref_directive_list.html#ref_list_nesting)
  - [Java程序员的注释](https://freemarker.apache.org/docs/ref_directive_list.html#ref_list_java_notes)





## 概要

列出序列（或集合）的最简单形式是：

```
<#list sequence as item>
    Part repeated for each item
</#list>
```

并列出散列的键值对（自2.3.25起）：

```
<#list hash as key, value>
    Part repeated for each key-value pair
</#list>
```

但这些只是通用形式的例子，如下所示。请注意，为简单起见，我们只显示序列表的通用表单; 只需将“ ” 替换为“ ”以获取哈希列表的通用形式。`as *item*``as *key*, *value*`

通用表格1：

```
<#list sequence as item>
    Part repeated for each item
<#else>
    Part executed when there are 0 items
</#list>
```

哪里：

- 该`else`部分是可选的，仅自FreeMarker 2.3.23起支持。
- `*sequence*`：表达式评估我们想要迭代的项目的序列或集合
- `*item*`：[循环变量的](https://freemarker.apache.org/docs/dgui_misc_var.html)名称（不是表达式）
- 标签之间的各种“部分”可以包含任意FTL（包括嵌套 `list`-s）

通用表单2（自FreeMarker 2.3.23起）：

```
<#list sequence>
    Part executed once if we have more than 0 items
    <#items as item>
        Part repeated for each item
    </#items>
    Part executed once if we have more than 0 items
<#else>
    Part executed when there are 0 items
</#list>
```

其中：见上面表格1的“Where”部分（因此该`else`部分也是可选的）。

## 描述

### 最简单的形式

假设`users`包含 `['Joe', 'Kate', 'Fred']`序列：

```
<#list users as user>
  <P> $ {用户}
</＃列表>
  <P>乔
  <P>凯特
  <P>佛瑞德
```

该`list`指令为指定为其第一个参数的序列（或集合）中的每个值执行`list`start-tag和 `list`end-tag（`list`从现在开始的主体 ）之间的代码。对于每个这样的迭代，循环变量（`user`在该示例中）将存储当前项的值。

循环变量（`user`）仅存在于`list`主体内部。此外，从循环内调用的宏/函数将看不到它（就好像它是一个局部变量）。

列表哈希非常相似，但是你需要提供两个变量名后面`as`; 一个用于散列键，另一个用于关联值。假设 `products`是`{ "apple": 5, "banana": 10, "kiwi": 15 }`：

```
<#list产品名称，价格>
  <p> $ {name}：$ {price}
</＃列表>
  苹果：5
  巴南：10
  <p>奇异果：15
```

请注意，并非所有哈希变量都可以列出，因为其中一些哈希变量无法枚举其键。尽管`Map`可以列出代表Java 对象的哈希值，但实际上是安全的。

### 其他指令





注意：

`else`内`list`因为FreeMarker的2.3.23只支持

`else`如果有0个项目，则必须打印特殊内容而不是仅打印任何内容时使用该指令：

```
<#list users as user>
  <P> $ {用户}
<#else伪>
  <p>没有用户
</＃列表>
```

这输出与前面的示例相同，除非 `users`包含0项：

```
  <p>没有用户
```

请注意，标记和 结束标记`user`之间不存在循环变量（），因为该部分不是循环的一部分。`else``list`

`else`必须在`list` 指令体内的字面意思（意思是，在源代码中）。也就是说，您无法将其移动到宏或包含的模板中。

### items指令





注意：

`items` 从FreeMarker 2.3.23开始存在

`items`如果您必须在第一个列表项之前打印（或执行）某些操作，并且在最后一个列表项之后，则使用该指令，只要至少有一个项目。一个典型的例子：

```
<#list users>
  <UL>
    <#items as user>
      <LI> $ {用户} </ LI>
    </＃项>
  </ UL>
</＃列表>
  <UL>
      <LI>乔</ LI>
      <LI>凯特</ LI>
      <LI>佛瑞德</ LI>
  </ UL>
```

如果有0个项目，则上面不会打印任何内容，因此您最终不会为空 `<ul></ul>`。

也就是说，当`list`指令没有 参数时，如果至少有一个项目，它的主体只执行一次，否则完全不执行。它是将为每个项运行的强制嵌套指令的主体，因此它也是 用于定义循环变量的指令，而不是 。`as *item*``items``items``as *item*``list`

一个`list`与指令 `items`也可以有一个`else` 指令：

```
<#list users>
  <UL>
    <#items as user>
      <LI> $ {用户} </ LI>
    </＃项>
  </ UL>
<#else伪>
  <p>没有用户
</＃列表>
```

更多细节：

- 解析器将检查一个`list` without 参数是否总是有一个嵌套 指令，并且一个指令总是有一个没有 参数的封闭。在解析模板时检查此项，而不是在执行模板时检查。因此，这些规则适用于FTL源代码本身，因此您无法移动 到宏或包含的模板中。`as*item*``items``items``list``as *item*``items`
- A `list`可以有多个 `items`指令，但只允许其中一个指令运行（只要你不离开并重新输入封闭`list`指令）; 并进一步尝试调用`items`将导致错误。因此，多个`items`可以在不同的被利用 `if`- `else`分行例如，而不是迭代两次。
- `items`指令不能有自己的嵌套`else`指令，只有封闭 `list`才能有
- 循环变量（`user`）仅存在于`items` 指令体内。

### sep指令





注意：

`sep` 从FreeMarker 2.3.23开始存在

`sep`当你必须在每个项目之间显示某些东西时（但不是在第一个项目之前或在最后一个项目之后），使用它。例如：

```
<#list users as user> $ {user} <#sep>， </＃list>
乔，凯特，弗雷德
```

以上`<#sep>, </#list>`是简写`<#sep>, </#sep></#list>`; 将`sep` 您是否把它放在封闭指令反正封闭结束标记可以被省略。在下一个示例中，您无法使用此类缩写（HTML标记不关闭任何内容，因为它们只是为FreeMarker输出的原始文本）：

```
<#list users as user>
  <DIV>
    $ {user} <#sep>，</＃sep>
  </ DIV>
</＃列表>
sep`只是一个简写 。因此，它可以在任何有 可用或循环变量的地方使用，它可以多次出现，并且它可以具有任意嵌套内容。`<#if *item*?has_next>...</#if>``list``items
```

解析器确保`sep`仅在具有可见循环变量的位置使用。这比模板的实际执行更早发生。因此，您无法`sep`从关联`list`或`items`指令内部 移动到宏或包含的模板（解析器无法知道将从哪里调用它们）。

### 中断指令





您可以使用该`break`指令随时退出迭代 。例如：

```
<#list 1..10 as x>
  $ {X}
  <#if x == 3>
    <#break>
  </＃如果>
</＃列表>
  1
  2
  3
```

该`break`指令可以在任何地方内放置`list`，只要它有 参数，否则它可以放在任何地方的内部 指令。但是，强烈建议在迭代中执行的所有其他操作之前或之后放置它。否则很容易在输出中结束未闭合的元素，否则会使模板更难理解。特别是，避免从自定义指令（如）的嵌套内容中脱离出来，因为指令的作者可能不会期望从不执行结束标记（）。`as *item*``items``<#list ...>...<@foo>...<#break>...</@foo>...</#list>``</@foo>`

如果`break`是在里面 `items`，它只会退出 `items`，而不是从`list`。通常，`break`只会退出为每个项调用其主体的指令，并且只能放在这样的指令中。所以例如不能使用 `break`inside `list`的 `else`部分，除非 `list`嵌套到另一个 `break`-able指令中。

使用`break`连同 `sep`通常是一个坏主意，因为 `sep`不知道你是否会跳过与项目的其他部分`break`，然后你结束了最后打印的项目之后的分隔符。

就像`else`和 `items`，`break`必须在字面意义上的指令体内突破，并且不能移出宏或包含模板。

### 继续指示





注意：

该`continue`指令自FreeMarker 2.3.27起存在

您可以使用指令跳过迭代主体的其余部分（直到`</#list>`或 `</#items>`标记的部分） `continue`，然后FreeMarker将继续下一个项目。例如：

```
<#list 1..5 as x>
  <#if x == 3>
    <#continue>
  </＃如果>
  $ {X}
</＃列表>
  1
  2
  4
  五
```

该`continue`指令可以在任何地方内放置`list`，只要它有 参数，否则它可以放在任何地方的内部 指令。但是，强烈建议将它放在迭代内所做的所有其他事情之前。否则很容易在输出中结束未闭合的元素，否则会使模板更难理解。特别是，避免从自定义指令（如）的嵌套内容中脱离出来，因为指令的作者可能不会期望从不执行结束标记（）。`as *item*``items``<#list ...>...<@foo>...<#continue>...</@foo>...</#list>``</@foo>`

调用时`continue`，`sep`不会为该迭代执行该 指令。使用`continue`连同 `sep`通常是一个坏主意，因为 `sep`不知道你是否会跳过其余项目，然后就结束了最后打印的项目之后的分隔符。

就像`break`， `continue`必须在指令的主体内部，其迭代需要“继续”，并且不能移出到宏或包含的模板中。

### 访问迭代状态

从2.3.23开始，[循环变量内置](https://freemarker.apache.org/docs/ref_builtins_loop_var.html)函数是访问迭代当前状态的首选方法。例如，这里我们使用`counter`和 `item_parity`循环变量内置函数（参见[参考文献中的](https://freemarker.apache.org/docs/ref_builtins_loop_var.html)所有内容）：

```
<#list users>
  <表>
    <#items as user>
      <tr class =“$ {user ？item_parity } Row”>
        <td> $ {user ？counter }
        <TD> $ {用户}
    </＃项>
  </ TABLE>
</＃列表>
  <表>
      <tr class =“ odd Row”>
        <td> 1
        <TD>乔
      <tr class =“ even Row”>
        <td> 2
        <TD>凯特
      <tr class =“ odd Row”>
        <td> 3
        <TD>佛瑞德
  </ TABLE>
```

在2.3.22和更早版本中，有两个额外的循环变量来检索迭代状态（它们仍然存在以便向后兼容）：

- `*item*_index` （*弃用*通过 `*item*?index`）：在循环的当前项的索引（从0开始编号）。
- `*item*_has_next` （*过时*的 `*item*?has_next`）：布尔值，告知用户，如果当前数据项为最后的序列或不在家。

所以在上面的例子中，你可以替换 `${user?counter}`为`${user_index + 1}`。

### 嵌套循环彼此

当然，`list`或者 `items`可以包含更多 `list`-s：

```
<#list 1..2 as i>
  <#list 1..3作为j>
    i = $ {i}，j = $ {j}
  </＃列表>
</＃列表>
    i = 1，j = 1
    i = 1，j = 2
    i = 1，j = 3
    i = 2，j = 1
    i = 2，j = 2
    i = 2，j = 3
```

它也允许使用冲突循环变量名称，如：

```
<#list 1..2 as i>
  外：$ {i}
  <#list 10..12 as i>
    内部：$ {i}
  </＃列表>
  外面再次：$ {i}
</＃列表>
  外面：1
    内心：10
    内心：11
    内心：12
  再次外面：1
  外面：2
    内心：10
    内心：11
    内心：12
  再次外面：2
```

### Java程序员的注释

如果经典兼容模式也 `list`接受标量并将其视为单元素序列。

如果传递一个包装了一个集合`java.util.Iterator`的 `list`，你可以在它的元素重复一次，因为`Iterator`S被其性质一次性的对象。当您尝试第二次列出此类集合变量时，错误将中止模板处理。