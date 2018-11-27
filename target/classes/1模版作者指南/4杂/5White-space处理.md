# White-space处理

页面内容

- [白空间剥离](https://freemarker.apache.org/docs/dgui_misc_whitespace.html#dgui_misc_whitespace_stripping)
- [使用compress指令](https://freemarker.apache.org/docs/dgui_misc_whitespace.html#autoid_30)

模板中[空白区域](https://freemarker.apache.org/docs/gloss.html#gloss.whiteSpace)的控制是一个问题，在某种程度上困扰着业务中的每个模板引擎。

让我们看看这个模板。我用颜色标记了模板的组件：文本，插值，FTL标记。使用*[BR]* -s，我可以看到[换行符](https://freemarker.apache.org/docs/gloss.html#gloss.lineBreak)。

```
<p>用户列表：[BR]
 <#assign users = [{“name”：“Joe”，“hidden”：false}，[BR] 
                  {“name”：“James Bond”，“hidden”：true }，[BR] 
                  {“name”：“Julia”，“hidden”：false}]> [BR] 
<ul> [BR]
 <#list users as user> [BR]
   <#if！user.hidden> [ BR] 
  <li> $ {user.name} [BR]
   </＃if> [BR]
 </＃list> [BR] 
</ ul> [BR] 
<p>这就是全部。
```

如果FreeMarker 按原样输出所有文本，则输出为：

```
<p>List of users:[BR]
[BR]
<ul>[BR]
[BR]
  [BR]
  <li>Joe[BR]
  [BR]
[BR]
  [BR]
[BR]
  [BR]
  <li>Julia[BR]
  [BR]
[BR]
</ul>[BR]
<p>That's all.
```

这里有很多不需要的空格和换行符。幸运的是，HTML和XML通常都不是白色空间敏感的，但是这些多余的空白区域可能很烦人，并且不必要地增加了生成的HTML的大小。当然，输出对空白敏感的格式时更是一个问题。

FreeMarker提供以下工具来解决此问题：

- 忽略模板文件的某些空白区域的工具 （解析时空白空间删除）：
  - 白色空间剥离：此功能会自动忽略FTL标记周围典型的多余空白区域。可以按模板方式启用或禁用它。
  - 微调指令：`t`， `rt`，`lt`。使用这些指令，您可以明确告诉FreeMarker忽略某些空白区域。阅读[参考资料](https://freemarker.apache.org/docs/ref_directive_t.html#ref.directive.t)以获取更多信息。
  - [`ftl`](https://freemarker.apache.org/docs/ref_directive_ftl.html#ref.directive.ftl) 参数`strip_text`：这将从模板中删除所有顶级文本。它仅对包含宏定义的模板（以及一些其他非输出指令）很有用，因为它删除了在宏定义之间和其他顶级指令之间使用的换行符，以提高模板的可读性。
- 从输出中删除空白区域的工具（即时空白移除）：
  - `compress` 指示。

## 白空间剥离 White-space stripping

如果为模板启用此功能，则它会自动忽略（即不打印到输出）两种典型的多余空白区域：

- 压痕空白，和尾部空白在该行的末尾（包括换行）将在只包含FTL标签线被忽略（例如 `<@myMacro/>`, `<#if *...*>`）和/或FTL注释（例如`<#-- blah -->`），除了被忽略的白色空间本身。例如，如果一行只包含一个`<#if *...*>`，那么标记之前的缩进和标记之后的换行将被忽略。但是，如果该行包含`<#if *...*>x`，那么该行中的空格将不会被忽略，因为 它不是FTL标记。请注意，根据这些规则，包含一行`<#if ...><#list ...> `受到空格忽略，而包含的行 `<#if *...*> <#list *...*>`不是，因为两个FTL标签之间的空白是嵌入的白色空间，而不是缩进或尾随空白。
- 夹在以下指令之间的空白会被忽略：`macro`，`function`， `assign`，`global`， `local`，`ftl`， `import`，但前提是 *只*白色的空间和/或指令之间的FTL意见。实际上，这意味着您可以在宏定义和赋值之间放置空行作为间距以提高可读性，而无需在输出中打印不必要的空行（换行符）。

启用了空格剥离的最后一个示例的输出将是：

```
<p>List of users:[BR]
<ul>[BR]
  <li>Joe[BR]
  <li>Julia[BR]
</ul>[BR]
<p>That's all.
```

这是因为剥离后模板变为以下内容; 被忽略的文本没有着色：

```
<p>List of users:[BR]
<#assign users = [{"name":"Joe",        "hidden":false},[BR]
                  {"name":"James Bond", "hidden":true},[BR]
                  {"name":"Julia",      "hidden":false}]>[BR]
<ul>[BR]
<#list users as user>[BR]
  <#if !user.hidden>[BR]
  <li>${user.name}[BR]
  </#if>[BR]
</#list>[BR]
</ul>[BR]
<p>That's all.
```

可以使用[`ftl`指令](https://freemarker.apache.org/docs/ref_directive_ftl.html#ref.directive.ftl)以模板方式启用/禁用空白空间剥离。如果未使用该`ftl`指令指定此项，则将启用或禁用空白空间，具体取决于程序员如何配置FreeMarker。出厂默认设置是启用了空白空间剥离，程序员可能会将其保留（推荐）。请注意，启用剥离空白并 *不会*降低模板执行的性能; 在模板加载期间完成空白空间剥离。

对于带有[`nt`](https://freemarker.apache.org/docs/ref_directive_nt.html#ref.directive.nt) 指令的单行，可以禁用空白空间剥离（对于No Trim）。

## 使用compress指令

另一种解决方案是使用该[`compress` 指令](https://freemarker.apache.org/docs/ref_directive_compress.html#ref.directive.compress)。与白色空间剥离相反，这直接在生成的输出上工作，而不是在模板上。也就是说，它会动态调查打印输出，而不会调查创建输出的FTL程序。它积极地删除缩进，空行和重复的空格/制表符（有关更多信息，请参阅[参考](https://freemarker.apache.org/docs/ref_directive_compress.html#ref.directive.compress)）。所以输出：

```
<#compress>
<#assign users = [{"name":"Joe",        "hidden":false},
                  {"name":"James Bond", "hidden":true},
                  {"name":"Julia",      "hidden":false}]>
List of users:
<#list users as user>
  <#if !user.hidden>
  - ${user.name}
  </#if>
</#list>
That's all.
</#compress>
```

将会：

```
List of users:
- Joe
- Julia
That's all.
```

请注意，`compress`它完全独立于空白空间剥离。因此，模板的空白区域可能会被剥离，之后生成的输出也会被删除 `compress`。

此外，默认情况下`compress`，在数据模型中可以使用用户定义的directve （由于向后兼容性）。这与指令相同，只是您可以选择设置`single_line` 参数，这将删除所有插入的换行符。如果你用 `<#compress>*...*</#compress>`最后一个例子替换`<@compress single_line=true>*...*</@compress>`，那么你得到这个输出：

```
List of users: - Joe - Julia That's all.
```