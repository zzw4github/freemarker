有四种标量类型：

- Boolean
- Number
- String
- Date-like (subtypes: date (no time part), time or date-time)

对于每个标量类型，都是TemplateTypeModel接口，其中 `*Type*`是类型的名称。 这些接口只定义了一个方法：type getAsType（）;. 这将返回具有Java类型的变量的值（分别为boolean，Number，String和Date）。

**note**

```

由于历史原因，字符串标量的接口称为TemplateScalarModel，而不是TemplateStringModel。 （这是因为早期的FreeMarker字符串是唯一一种标量。）
```

这些接口的简单实现可以在带有SimpleType类名的freemarker.template包中找到。 但是，没有SimpleBooleanModel; 要表示布尔值，可以使用TemplateBooleanModel.TRUE和TemplateBooleanModel.FALSE单例。

注意：

```
由于历史原因，字符串标量的类称为SimpleScalar，而不是SimpleString。
```

标量在FTL中是不可变的。 在模板中设置变量的值时，则将TemplateTypeModel实例替换为另一个实例，并且不要更改存储在原始实例中的值

##### “类似日期”类型的困难

围绕类似日期的类型存在复杂性，因为Java API通常不区分只存储日期部分的java.util.Date-s（2003年4月4日），只有时间部分（晚上10:19:18），或两者兼而有之（2003年4月4日10:19:18 PM）。要正确显示值（或执行某些其他操作），FreeMarker必须知道java.util.Date的哪些部分存储有意义的信息，什么部分未使用（通常是0-ed）。不幸的是，这些信息通常仅在值来自数据库时才可用，因为大多数数据库都有单独的日期，时间和日期时间（又称时间戳）类型，和java.sql有3个对应的java.util.Date子类。

TemplateDateModel接口有两个方法：java.util.Date getAsDate（）和int getDateType（）。此接口的典型实现，存储java.util.Date对象，加上一个告诉子类型的整数。此整数的值必须是TemplateDateModel接口的常量：DATE，TIME，DATETIME和UNKNOWN。

关于UNKNOWN：java.lang和java.util类通常自动转换为TemplateModel实现的ObjectWrapper（参见前面的对象包装）。如果对象包装器必须包装java.util.Date，它不是java.sql日期类的实例，则它无法确定子类型是什么，因此它使用UNKNOWN。 后来，如果模板必须使用此变量，并且操作需要子类型，则它将因错误而停止。为了防止这种情况，对于有问题的变量，模板作者必须使用日期，时间或日期时间内置命令（如lastUpdated？datetime）明确指定子类型。请注意，如果使用带有format参数的字符串，如foo？string [“MM / dd / yyyy”]，则FreeMarker不需要知道子类型。
