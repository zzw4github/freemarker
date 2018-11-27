# 将FreeMarker与servlet一起使用

页面内容

- [使用FreeMarker进行“模型2”](https://freemarker.apache.org/docs/pgui_misc_servlet.html#pgui_misc_servlet_model2)
- [包括来自其他Web应用程序资源的内容](https://freemarker.apache.org/docs/pgui_misc_servlet.html#pgui_misc_servlet_include)
- [在FTL中使用JSP自定义标记](https://freemarker.apache.org/docs/pgui_misc_servlet.html#autoid_63)
- [将FTL嵌入到JSP页面中](https://freemarker.apache.org/docs/pgui_misc_servlet.html#autoid_64)



从根本上讲，在Web应用程序空间中使用FreeMarker与其他任何地方都没有区别; FreeMarker将其输出写入`Writer`您传递给`Template.process`方法的输出，并不关心是否`Writer`打印到控制台或文件或输出流`HttpServletResponse`。FreeMarker对servlet和Web一无所知; 它只是将Java对象与模板文件合并，并从中生成文本输出。从这里开始，您可以自行决定如何构建Web应用程序。

但是，您可能希望将FreeMarker与一些现有的Web应用程序框架一起使用。许多框架依赖于“模型2”体系结构，其中JSP页面处理表示。如果您使用这样的框架（例如，[Apache Struts](http://jakarta.apache.org/struts)），那么请继续阅读。对于其他框架，请参阅框架的文档。

## 使用FreeMarker进行“模型2”

许多框架遵循HTTP请求被调度到任何将数据放入用户定义的“动作”的类的策略`ServletContext`， `HttpSession`和 `HttpServletRequest`对象作为属性，则该请求是由框架转发到JSP页面（视图），将产生的HTML页面使用随属性发送的数据。这通常称为模型2。

![数字](https://freemarker.apache.org/docs/figures/model2sketch.png)

使用这些框架，您可以简单地使用FTL文件而不是JSP文件。但是，由于您的servlet容器（Web应用程序服务器）与JSP文件不同，因此不了解如何处理FTL文件，因此Web应用程序需要进行一些额外的配置：

1. 将`freemarker.jar`（从 `lib`FreeMarker发行版的`WEB-INF/lib`目录）复制到Web应用程序的目录中。
2. 将以下部分插入`WEB-INF/web.xml`Web应用程序的 文件（并根据需要进行调整）：

```
的<servlet>
  <servlet的名称>的freemarker </ servlet的名称>
  <servlet-class> freemarker.ext.servlet.FreemarkerServlet </ servlet-class>

  <！ - 
    Init-param文档：
    https://freemarker.apache.org/docs/api/freemarker/ext/servlet/FreemarkerServlet.html
  - >

  <！ -  FreemarkerServlet设置： - >
  <INIT-PARAM>
    <PARAM名称> TEMPLATEPATH </ PARAM名称>
    <PARAM值> / </ PARAM值>
  </ INIT-param>
  <INIT-PARAM>
    <PARAM名称> NoCache的</ PARAM名称>
    <PARAM值>真</ PARAM值>
  </ INIT-param>
  <INIT-PARAM>
    <PARAM名称> ResponseCharacterEncoding </ PARAM名称>
    <！ - 使用FreeMarker的output_encoding设置： - >
    <PARAM值> fromTemplate </ PARAM值>
  </ INIT-param>
  <INIT-PARAM>
    <PARAM名称> ExceptionOnMissingTemplate </ PARAM名称>
    缺少模板上的<！ -  true => HTTP 500，而不是HTTP 404.  - >
    <PARAM值>真</ PARAM值>
  </ INIT-param>

  <！ -  FreeMarker引擎设置： - >
  <INIT-PARAM>
    <PARAM名称> incompatible_improvements </ PARAM名称>
    <PARAM值> 2.3.27 </ PARAM值>
    <！ - 
      建议设置为高值。
      请参阅：https：//freemarker.apache.org/docs/pgui_config_incompatible_improvements.html
    - >
  </ INIT-param>
  <INIT-PARAM>
    <PARAM名称> template_exception_handler </ PARAM名称>
    <！ - 在开发过程中使用“html_debug”！ - >
    <PARAM值>重新抛出</ PARAM值>
  </ INIT-param>
  <INIT-PARAM>
    <PARAM名称> template_update_delay </ PARAM名称>
    <！ - 在开发过程中使用0！考虑一下你需要什么价值。 - >
    <param-value> 30 s </ param-value>
  </ INIT-param>
  <INIT-PARAM>
    <PARAM名称> default_encoding </ PARAM名称>
    <！ - 模板文件的编码： - >
    <PARAM值> UTF-8 </ PARAM值>
  </ INIT-param>
  <INIT-PARAM>
    <PARAM名称> output_encoding </ PARAM名称>
    <！ - 模板输出的编码; 请注意，您必须设置
         “ResponseCharacterEncodring”到“fromTemplate”为此工作！ - >
    <PARAM值> UTF-8 </ PARAM值>
  </ INIT-param>
  <INIT-PARAM>
    <PARAM名称>区域设置</ PARAM名称>
    <！ - 影响数字和日期/时间格式等 - >
    <PARAM值> EN_US </ PARAM值>
  </ INIT-param>
  <INIT-PARAM>
    <PARAM名称> number_format </ PARAM名称>
    <PARAM值> 0。########## </ PARAM值>
  </ INIT-param>

  <负载上启动> 1 </负载上启动>
</ servlet的>

<servlet的映射>
  <servlet的名称>的freemarker </ servlet的名称>
  <url-pattern> * .ftl </ url-pattern>
  <！ - 如果incompatible_improvements> = 2.3.24，则自动转义HTML和XML： - >
  <url-pattern> * .ftlh </ url-pattern>
  <url-pattern> * .ftl x </ url-pattern>
</ servlet的映射>

...

<！ - 
  阻止从servlet容器外部访问MVC视图。
  RequestDispatcher.forward / include应该，并且仍然有效。
  删除它可能会打开安全漏洞！
- >
<安全约束>
  <网络资源收集>
    <web-resource-name> FreeMarker MVC Views </ web-resource-name>
    <URL模式> *。FTL </ URL模式>
    <URL模式> *。ftlh </ URL模式>
    <URL模式> *。ftlx </ URL模式>
  </网络资源集合>
  <AUTH-约束>
    <！ - 没有人被允许直接访问这些。 - >
  </ AUTH-约束>
</安全约束>
```

在此之后，您可以使用`*.ftl`与JSP（`*.jsp`）文件相同的方式使用FTL文件（）。（当然你可以选择另一个扩展 `ftl`;它只是常规）

注意：

它是如何工作的？让我们来看看JSP-s的工作原理。许多servlet容器使用映射到`*.jsp`请求URL模式的servlet处理JSP-s 。该servlet将接收请求URL结束的所有请求，根据请求URL `.jsp`查找JSP文件，并在内部将其编译为a `Servlet`，然后调用生成的servlet以生成页面。这里`FreemarkerServlet`映射到 `*.ftl`URL模式的方法是相同的，除了FTL文件没有编译成`Servlet`-s，而是编译到 `Template`对象，然后 调用`process`方法`Template`来生成页面。

例如，代替这个JSP文件（注意它大量使用Struts标记-libs来保存设计者从嵌入式Java怪物）：

```
<％@ taglib uri =“/ WEB-INF / struts-bean.tld”prefix =“bean”％>
<％@ taglib uri =“/ WEB-INF / struts-logic.tld”prefix =“logic”％>

<HTML>
<head> <title> Acmee Products International </ title>
<BODY>
  <h1> Hello <bean：write name =“user”/>！</ h1>
  <p>以下是我们的最新优惠：
  <UL>
    <logic：iterate name =“latestProducts”id =“prod”>
      <li> <bean：write name =“prod”property =“name”/>
        for <bean：write name =“prod”property =“price”/> Credits。
    </逻辑：迭代>
  </ UL>
</ BODY>
</ HTML>
```

你可以使用这个FTL文件（使用`ftl`文件扩展名代替`jsp`）：

```
<HTML>
<head> <title> Acmee Products International </ title>
<BODY>
  <h1> Hello $ {user}！</ h1>
  <p>以下是我们的最新优惠：
  <UL>
    <#list latestProducts as prod>
      <li> $ {prod.name} for $ {prod.price} Credits。
    </＃列表>
  </ UL>
</ BODY>
</ HTML>
```

警告！

在FreeMarker 中只是静态文本，因此它会像任何其他XML或HTML标记一样按原样打印到输出。JSP标签只是FreeMarker指令，没什么特别的，所以你*使用FreeMarker语法*来调用它们，而不是JSP语法： 。请注意，在FreeMarker语法中，您*不像* JSP中*那样使用* *in参数*，*也不引用参数值*。所以这是 *错误的*：`<html:form action="/query">*...*</html:form>``<@html.form action="/query">*...*</@html.form>`*${...}*

```
<＃ - 错误： - >
<@ my.jspTag color =“$ {aVariable}”name =“aStringLiteral”
            width =“100”height = $ {a + b} />
```

这很好：

```
<＃ - 好： - >
<@ my.jspTag color = aVariable name =“aStringLiteral”
            width = 100 height = a + b />
```

在两个模板中，当您引用`user` 和时`latestProduct`，它将首先尝试查找在模板中创建的具有该名称的变量（例如 `prod`;如果您掌握JSP：页面范围属性）。如果失败了，它将尝试查找具有该名称的属性`HttpServletRequest`，如果它不在那里，那么`HttpSession`，如果它仍然没有找到它，那么在`ServletContext`。在FTL的情况下，这是有效的，因为`FreemarkerServlet` 从所提到的3个对象的属性构建数据模型。也就是说，在这种情况下，根哈希不是a `java.util.Map`（就像在本手册中的某些示例代码中那样），而是 `ServletContext`+ `HttpSession`+`HttpServletRequest`; FreeMarker对数据模型的内容非常灵活。因此，如果您想将变量`"name"`放入数据模型中，那么您可以调用 `servletRequest.setAttribute("name", "Fred")`; 这是模型2的逻辑，FreeMarker适应它。

`FreemarkerServlet`还将3个哈希值放入数据模型中，通过它可以直接访问3个对象的属性。哈希变量是： `Request`，`Session`，`Application`（对应 `ServletContext`）。它还公开了另一个名为的哈希`RequestParameters`，它提供了对HTTP请求参数的访问。

`FreemarkerServlet`有各种init-params。它可以设置为从任意目录，类路径或相对于Web应用程序目录加载模板。您可以设置用于模板的字符集，模板使用的默认语言环境，要使用的对象包装器等。

`FreemarkerServlet`通过子类化可以轻松地满足特殊需求。比如，如果您需要在数据模型中为所有模板提供其他变量，请将servlet子类化并覆盖该 `preTemplateProcess()`方法，以便在处理模板之前将所需的任何其他数据推送到模型中。或子类的servlet，并设置这些全球可用的变量作为[共享变量](https://freemarker.apache.org/docs/pgui_config_sharedvariables.html)中`Configuration`。

有关更多信息，请阅读该类的Java API文档。

## 包括来自其他Web应用程序资源的内容

您可以使用（自2.3.15开始）`<@include_page path="..."/>`提供的自定义指令 `FreemarkerServlet`将另一个Web应用程序资源的内容包含到输出中; 这通常有助于将JSP页面的输出（与同一Web服务器中的FreeMarker模板一起生成）集成到FreeMarker模板输出中。使用：

```
<@include_page path =“path / to / some.jsp”/>
```

与在JSP中使用此标记完全相同：

```
<jsp：include page =“path / to / some.jsp”>
```

注意：

`<@include_page ...>`不要混淆`<#include ...>`，因为最后一个是包含FreeMarker模板而不涉及Servlet容器。一个`<#include ...>`-ed模板共享与包括模板，如数据模型和模板语言变量模板处理状态，而`<@include_page ...>` 启动一个独立的HTTP请求的处理。

注意：

一些Web应用程序框架为此提供了自己的解决方案，在这种情况下，您可能应该使用它。某些Web应用程序框架也不使用 `FreemarkerServlet`，因此 `include_page`不可用。

路径可以是相对的或绝对的。相对路径相对于当前HTTP请求的URL（触发模板处理的URL）进行解释，而绝对路径在当前servlet上下文（当前Web应用程序）中是绝对路径。您不能包含当前Web应用程序之外的页面。请注意，您可以包含任何页面，而不仅仅是JSP页面; 我们只使用了以路径结尾的页面`.jsp`作为插图。

除了`path`参数之外，您还可以指定一个以`inherit_params`布尔值命名的可选参数 （未指定时默认为true），该参数指定包含的页面是否将查看当前请求的HTTP请求参数。

最后，您可以指定一个名为的可选参数`params`，该参数 指定包含的页面将看到的新请求参数。如果传递了继承的参数，则指定参数的值将被添加到同名的继承参数的值之前。值 `params`必须是哈希值，其中的每个值都是字符串或字符串序列（如果需要多值参数）。这是一个完整的例子：

```
<@include_page path =“path / to / some.jsp”inherit_params = true params = {“foo”：“99”，“bar”：[“a”，“b”]} />
```

这将包括页面 `path/to/some.jsp`，传递当前请求的所有请求参数，除了“foo”和“bar”，它们将分别设置为“99”和多值“a”，“b”。如果原始请求已具有这些参数的值，则新值将添加到现有值之前。即如果“foo”具有值“111”和“123”，则它现在将具有值“99”，“111”，“123”。

事实上，可以为其中的参数值传递非字符串值 `params`。这样的值将首先转换为合适的Java对象（即Number，Boolean，Date等），然后使用其Java `toString()`方法获取字符串值。不过，最好不要依赖这种机制，而是明确地确保非字符串的参数值转换为模板级别的字符串，您可以使用`?string`和`?c`内置控件对格式进行控制 。

## 在FTL中使用JSP自定义标记

`FreemarkerServlet`将 `JspTaglibs`哈希放入数据模型中，您可以使用它来访问JSP标记库。JSP自定义标记可以作为普通的用户定义指令访问，自定义EL函数（因为FreeMarker 2.3.22）可以作为方法访问。例如，对于此JSP文件：

```
<％@ page contentType =“text / html; charset = ISO-8859-2”language =“java”％>
<％@ taglib prefix =“e”uri =“/ WEB-INF / example.tld”％>
<％@ taglib prefix =“oe”uri =“/ WEB-INF / other-example.tld”％>
<％@ taglib prefix =“c”uri =“http://java.sun.com/jsp/jstl/core”％>
<％@ taglib prefix =“fn”uri =“http://java.sun.com/jsp/jstl/functions”％>

<％ - 自定义JSP标记和函数： - ％>

<e：someTag numParam =“123”boolParam =“true”strParam =“Example”anotherParam =“$ {someVar}”>
  ...
</ E：someTag>

<oe：otherTag />

$ {e：someELFunction（1,2）}


<％ -  JSTL： - ％>

<c：if test =“$ {foo}”>
  做这个
</ C：如果>

<C：选择>
  <c：当test =“$ {x == 1}”>时
      做这个
  </ C：当>
  <C：否则>
      去做
  </ C：否则>
</ C：选择>

<c：forEach var =“person”items =“$ {persons}”>
  $ {} person.name
</ C：的forEach>

$ {FN：修剪（巴）}
```

约等效的FTL是：

```
<#assign e = JspTaglibs [“/ WEB-INF / example.tld”]>
<#assign oe = JspTaglibs [“/ WEB-INF / other-example.tld”]>

<＃ - 自定义JSP标记和函数： - ＃>

<@ e.someTag numParam = 123 boolParam = true strParam =“Example”anotherParam = someVar>
  ...
</ @ e.someTag>

<@ oe.otherTag />

$ {e.someELFunction（1,2）}


<＃ -  JSTL  - 相反，使用本机FTL结构： - >

<#if foo>
  做这个
</＃如果>

<#if x == 1>
  做这个
<#else伪>
  去做
</＃如果>

<#list person as person>
  $ {} person.name
</＃列表>

$ {吧？修剪}
```

注意：

参数值在JSP中不使用引号 等。稍后再看解释。`"${*...*}"`

注意：

`JspTaglibs`不是FreeMarker的核心功能; 它仅在通过调用模板时存在 `FreemarkerServlet`。这是因为JSP标记/函数假定一个servlet环境（FreeMarker没有），还有一些Servlet概念必须在`FreemarkerServlet` 构建的特殊FreeMarker数据模型中进行模拟。许多现代框架以纯粹的方式使用FreeMarker，而不是通过`FreemarkerServlet`。

由于JSP自定义标记是在JSP环境中编写的，因此它们假设变量（在JSP世界中通常称为“bean”）存储在4个范围中：页面范围，请求范围，会话范围和应用程序范围。FTL没有这样的表示法（4个作用域），但 `FreemarkerServlet`为自定义JSP标记提供了模拟的JSP环境，它维护了JSP作用域和FTL变量的“bean”之间的对应关系。对于自定义JSP标记，请求，会话和应用程序范围与实际JSP完全相同：和`javax.servlet.ServletContext`， `HttpSession`和的属性 `ServletRequest` 对象。从FTL方面，您可以将这三个范围视为数据模型，如前所述。页面范围对应于FTL全局变量（请参阅[`global` 指令](https://freemarker.apache.org/docs/ref_directive_global.html#ref.directive.global)）。也就是说，如果使用`global`指令创建变量 ，则通过模拟的JSP环境将自定义标记作为页面范围变量可见。此外，如果JSP标记创建新的页面范围变量，则结果与使用。创建变量的结果相同 `global`指示。请注意，数据模型中的变量作为JSP标记的页面范围属性不可见，尽管它们是全局可见的，因为数据模型对应于请求，会话和应用程序范围，而不是页面范围。

在JSP页面上，您引用所有属性值，如果参数的类型是字符串或布尔值或数字，则它不会匹配。但由于自定义标记在FTL模板中可作为用户定义的FTL指令访问，因此您必须在自定义标记内使用FTL语法规则，而不是JSP规则。因此，当您指定“属性”的值时，则在右侧 `=`有一个[FTL表达式](https://freemarker.apache.org/docs/dgui_template_exp.html)。因此， *您不能引用布尔值和数值参数值*（例如`<@tiles.insert page="/layout.ftl" flush=true/>`），或者它们被解释为字符串值，当FreeMarker尝试将值传递给期望非字符串值的自定义标记时，这将导致类型不匹配错误。还要注意，当然，您可以使用任何FTL表达式作为属性值，例如变量，计算值等（例如`<@tiles.insert page=layoutName flush=foo && bar/>`）。

FreeMarker在使用JSP标记库时不依赖于运行它的servlet容器的JSP支持，因为它实现了自己的轻量级JSP运行时环境。只需要注意一个小细节：要使FreeMarker JSP运行时环境能够将事件分派到在其TLD文件中注册事件侦听器的JSP标记库，您应该将其添加到`WEB-INF/web.xml`Web应用程序中：

```
<听者>
  <监听级> freemarker.ext.jsp.EventForwarding </监听级>
</听众>
```

请注意，即使servlet容器没有本机JSP支持，也可以将JSP标记库与FreeMarker一起使用，只需确保`javax.servlet.jsp.*`您的Web应用程序可以使用JSP 2.0（或更高版本）的 包。

在撰写本文时，除了JSP 2的“标记文件”功能（即，用JSP语言*实现的*自定义JSP标记）之外，还实现了JSP 2.1之前的JSP功能。必须将标记文件编译为Java类才能在FreeMarker下使用。

`JspTaglibs[*uri*]` 必须找到指定URI的TLD，就像JSP的`@taglib`指令一样 。为此，它实现了JSP规范中描述的TLD发现机制。在那里看到更多，但简而言之，它在`WEB-INF/web.xml` `taglib` 元素，at `WEB-INF/**/*.tld`和in中 搜索TLD-s `WEB-INF/lib/*.{jar,zip}/META-INF/**/*.tld`。此外，它可以发现类加载器可见的TLD-s，即使它们在WAR结构之外，当您使用`MetaInfTldSources`和/或`ClasspathTlds` `FreemarkerServlet`init-params 设置它时（自2.3.22起）。请参阅Java API文档`FreemarkerServlet` 对于这些的描述。也可以从Java系统属性中设置这些属性，当您想要在Eclipse运行配置中更改这些属性而不修改它们时，这可以很方便 `web.xml`。再次，请参阅 `FreemarkerServlet`API文档。 `FreemarkerServlet`还可以识别`org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern` servlet上下文属性，并将其中的条目添加到`MetaInfTldSources`。

## 将FTL嵌入到JSP页面中

有一个taglib允许您将FTL片段放入JSP页面。嵌入式FTL片段可以访问4个JSP作用域的属性（Beans）。您可以在FreeMarker发行版中找到一个工作示例和taglib。