## 概要

```
<#attempt>
  attempt block
<#recover>
  recover block
</#attempt>
```

哪里：

- `*attempt block*`：包含任何内容的模板块。这将始终执行，但如果在此期间发生错误，则回滚此块的所有输出，并将 `*recover block*`执行。
- `*recover block*`：包含任何内容的模板块。只有在执行期间出现错误时才会执行此操作`*attempt block*`。您可以在此处打印错误消息等。

这`*recover*`是强制性的。`attempt`/ `recover`可以自由嵌套到其他`*attempt block*`s或`*recover block*`s中。

注意：

从2.3.3开始支持此处显示的格式; 早些时候 ，它仍然支持向后兼容性。此外，这些指令是在FreeMarker 2.3.1中引入的，因此它们不存在于2.3中。`<#attempt>*...*<#recover>*...*</#recover>`

## 描述

如果您希望页面成功输出，即使页面的某个部分输出失败，也会使用这些指令。如果在执行期间发生错误 `*attempt block*`，则`*attempt block*`回滚输出（并记录错误，至少使用默认配置），然后 `*recover block*`执行，然后在执行后正常继续执行模板`*recover block*`。如果在执行期间没有发生错误`*attempt block*`，则`*recover block*`忽略该错误 。一个简单的例子：

```
主要内容
<#attempt> 
  可选内容：$ {thisMayFails} 
<#recover> 
  Ops！可选内容不可用。
</＃attempt> 
主要内容继续
```

如果`thisMayFails`变量不存在（或在该位置发生任何其他错误），则输出为：

```
主要内容
  Ops！可选内容不可用。
主要内容继续
```

如果`thisMayFails`变量存在且值为`123`，则输出为：

```
主要内容
  可选内容：123 
主要内容继续
```

它`*attempt block*`具有全有或全无语义：`*attempt block*`输出的整个内容（当没有错误时），或者执行时没有输出 `*attempt block*`（当出现错误时）。例如，上面，在“可选内容：”打印后发生故障，但在“Ops！”之前的输出中仍然没有。（这是通过积极缓冲内部输出来实现的`*attempt block*`。甚至`flush` 指令也不会将输出发送到客户端。）

为了防止来自上面例子的误解： `attempt`/ `recover`不是（仅）用于处理未定义的变量（对于那个使用[缺失值处理程序操作符）](https://freemarker.apache.org/docs/dgui_template_exp.html#dgui_template_exp_missing)）。它可以处理执行块时发生的所有类型的错误（即，不是先前检测到的语法错误）。它意味着包含更大的模板片段，其中错误可能发生在各个点。例如，您在模板中有一部分处理打印广告，但这不是页面的主要内容，因此您不希望整个页面因为广告打印时出现错误（例如，因为数据库服务器中断）。所以你把整个广告打印放到了 `*attempt block*`。

在某些环境中，程序员配置FreeMarker，以便它不会因某些错误而中止模板执行，但可能会在将一些错误指示符打印到输出后继续执行（请参阅[此处...](https://freemarker.apache.org/docs/pgui_config_errorhandling.html)）。该 `attempt`指令不将这种被抑制的错误视为错误。

在[特殊变量中](https://freemarker.apache.org/docs/ref_specvar.html)`*recover block*`可以使用错误的错误消息。不要忘记对特殊变量的引用是以点开头的（例如:) 。`error` `${.error}`

默认情况下，内部正在发生的错误`*attempt block*`的[记录](https://freemarker.apache.org/docs/pgui_misc_logging.html)与 `ERROR`水平，尽管该模板从中恢复。这是因为`attempt`它不是一个通用的错误处理程序机制，就像 `try`在Java中一样。这是为了减少意外错误对访问者的影响，使得只有部分页面可能会关闭，而不是整个页面。但它仍然是一个错误，需要操作员注意。（报告此错误的方式可以使用`attempt_exception_reporter`配置设置进行自定义 ，因为FreeMarker 2.3.27。）