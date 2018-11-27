

# setting

页面内容

- [概要](https://freemarker.apache.org/docs/ref_directive_setting.html#autoid_116)
- [描述](https://freemarker.apache.org/docs/ref_directive_setting.html#autoid_117)





## 概要

```
<#setting name=value>
```

哪里：

- `*name*`：设置的名称。这不是表达！
- `*value*`：设置的新值。表达

## 描述

设置进一步处理的设置。设置是影响FreeMarker行为的值。新值仅出现在设置它的模板处理中，并且不会触及模板本身。设置的初始值由编程器设置（参见：[程序员指南/配置/设置](https://freemarker.apache.org/docs/pgui_config_settings.html)）。

支持的设置是：

- `locale`：输出的区域设置（语言）。它可以影响数字，日期等的表示格式。该值是一个字符串，由一个语言代码（小写双字母ISO-639代码）和可选的县代码（大写双字母ISO-3166代码）组成带有下划线的语言代码，如果我们指定了国家/地区，那么可选的变体代码（非标准化）与带有下划线的国家/地区分开。有效值的例子：`en`，`en_US`， `en_US_MAC`。FreeMarker将尝试使用最具体的可用语言环境，因此如果您指定 `en_US_MAC`但未知，那么它会尝试`en_US`，然后`en`，然后是计算机的默认语言环境（可由程序员设置）。

- `number_format`：未指定显式格式时用于将数字转换为字符串的数字格式。可以是以下之一：

  - Java平台定义的预定义值:( `number`默认值） `currency`，或 `percent`
  - `computer`，其格式，如[在`c` 内置](https://freemarker.apache.org/docs/ref_builtins_number.html#ref_builtin_c)
  - 例如，以 [Java十进制数格式语法](http://java.sun.com/j2se/1.4/docs/api/java/text/DecimalFormat.html)编写的格式模式`0.###`。FreeMarker [扩展了这种格式](https://freemarker.apache.org/docs/ref_builtins_number.html#topic.extendedJavaDecimalFormat)，允许指定舍入模式，使用的符号等。
  - `@`以该字母开头的值后跟一个字母，请参考[自定义格式](https://freemarker.apache.org/docs/pgui_config_custom_formats.html)。例如，`"@price"`指的是使用`"price"` 名称注册的自定义格式。自定义格式名称后面可能是空格或`_`格式参数，其解释取决于自定义格式。为了向后兼容，初始`@`如果任只有这个新的含义[的`incompatible_improvements`设置](https://freemarker.apache.org/docs/pgui_config_incompatible_improvements.html#pgui_config_incompatible_improvements_how_to_set) 是至少2.3.24，或者有定义的任何自定义格式。当初始`@`后面没有一个字母（任何UNICODE字母），它从未被视为对自定义格式的引用。

- `boolean_format`：逗号分隔的字符串对，分别用于表示true和false值，用于在未指定显式格式时将布尔值转换为字符串（如in ）。请注意，当前没有从此字符串中删除空格，因此请勿在逗号后面添加空格。默认值为 ，但FreeMarker将拒绝使用该特定值 ，并且需要使用（这从2.3.21开始工作）。对于任何其它的值，如 ， 将工作。另请参见：[ 内置](https://freemarker.apache.org/docs/ref_builtins_boolean.html#ref_builtin_string_for_boolean)。`${*booleanValue*}``"true,false"``${*booleanValue*}``${*booleanValue*?c}``"Y,N"``${*booleanValue*}`[`string`](https://freemarker.apache.org/docs/ref_builtins_boolean.html#ref_builtin_string_for_boolean)

- `date_format`， `time_format`， `datetime_format`：用于日期/时间/日期时间值（Java的转换格式 `java.util.Date`时，通过未规定明确的格式-s和它的子类）为字符串的[`string` 内置](https://freemarker.apache.org/docs/ref_builtins_date.html#ref_builtin_string_for_date)（或其他方式），在的情况下 `${someDate}`。该 `date_format`设置仅影响不存储时间部分的日期值的格式， `time_format`仅影响不存储日期部分`datetime_format`的时间格式，并且 仅影响日期时间值的格式。这些设置也影响什么格式做的 [`?time`， `?date`和 `?datetime`](https://freemarker.apache.org/docs/ref_builtins_string.html#ref_builtin_string_date) 期待它应用于字符串值时。

  可能的设置值是（引号不是值本身的一部分）：

  - 例如[，Java接受的`SimpleDateFormat`](http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html)模式`"dd.MM.yyyy HH:mm:ss"`（其中 `"HH"`表示0-23小时）或 `"MM/dd/yyyy hh:mm:ss a"`（ `"a"`如果当前语言为英语，则打印AM或PM）。

    警告！

    注意*不要*使用 `YYYY`（大写，表示“周年”）而不是`yyyy`（小写，表示年份）！这是一个容易犯的错误，在测试过程中很难注意到，因为“周年”只与年代边缘的“年份”不同。

  - `"xs"`适用于XML Schema格式或 `"iso"`ISO 8601：2004格式。这些格式允许使用空格分隔的各种附加选项，如`"iso m nz"`（或与... `_`一样`"iso_m_nz"`;这在类似情况下很有用 `lastModified?string.iso_m_nz`）。选项及其含义是：

    - 准确度选项：
      - `ms`：毫秒，总是以全部3位数显示，即使它都是0-s。例：`13:45:05.800`
      - `s`：秒（即使非0也会丢弃分数秒），如 `13:45:05`
      - `m`：分钟，比如 `13:45`。这是不允许的 `"xs"`。
      - `h`：小时，就像 `13`。这是不允许的 `"xs"`。
      - 两者都没有：高达毫秒的精度，但是尾随毫秒0-s被移除，否则整个毫秒部分如果它将为0。例：`13:45:05.8`
    - 时区偏移可见性选项：
      - `fz`：“强制区域”，始终显示时区偏移（即使for `java.sql.Date`和 `java.sql.Time`值）。但是，由于ISO 8601不允许显示区域偏移的日期（表示没有时间的日期），因此对于日期，此选项将不起作用 `"iso"`。
      - `nz`：“无区域”，从不显示时区偏移
      - 两者都不：始终显示时区偏移量，除了`java.sql.Date`和 `java.sql.Time`，以及 `"iso"`日期值。
    - 时区选项：
      - `u`：使用UTC而不是`time_zone`设置建议的内容。但是，`java.sql.Date`并且 `java.sql.Time`不受此影响（请参阅 `sql_date_and_time_time_zone`了解原因）
      - `fu`：“强制UTC”，即使用UTC而不是 设置`time_zone`或 `sql_date_and_time_time_zone`设置建议的内容。这也是影响 `java.sql.Date`和 `java.sql.Time`价值
      - 两者都不使用：使用`time_zone`或 `sql_date_and_time_time_zone` 配置设置 建议的时区

    来自同一类别的选项是互斥的，例如使用`m`和`s` 一起是错误。

    可以按任何顺序指定选项。

    精度和时区偏移可见性选项不会影响解析，只会影响格式化。例如，即使您使用`"iso m nz"`， `"2012-01-01T15:30:05.125+01"`也将成功解析并具有毫秒精度。时区选项（例如`"u"`）仅在解析不包含时区偏移的字符串时影响选择的时区。

    解析同时`"iso"`理解“扩展格式”和“基本格式” `20141225T235018`。但是，它不支持解析所有类型的ISO 8601字符串：如果有日期部分，则必须使用月份，月份和日期值（不是一年中的某一周），并且当天不能被省略。

    `"iso"`故意输出，因此它也是XML Schema格式的一个很好的代表，除了0和负数年，它是不可能的。另请注意，`"iso"` 格式中的日期值会省略时区偏移，而为`"xs"` 格式保留时区偏移。

  - `"short"`， `"medium"`，`"long"`，或 `"full"`，其具有由Java平台定义的区域设置相关的意义（见在[的文档`java.text.DateFormat`](http://docs.oracle.com/javase/7/docs/api/java/text/DateFormat.html)）。对于日期时间值，您可以单独指定日期和时间部分的长度，将它们分开 `_`，例如`"short_medium"`。（`"medium"`意味着 `"medium_medium"`为日期时间值。）

  - `@`以该字母开头的值后跟一个字母，指的是[自定义格式](https://freemarker.apache.org/docs/pgui_config_custom_formats.html)，例如`"@worklog"`指的是使用`"worklog"` 名称注册的自定义格式。格式名称可能后跟空格或 `_`格式参数，其解释取决于自定义格式。为了向后兼容，初始`@`如果任只有这个新的含义[的`incompatible_improvements`设置](https://freemarker.apache.org/docs/pgui_config_incompatible_improvements.html#pgui_config_incompatible_improvements_how_to_set) 是至少2.3.24，或者有定义的任何自定义格式。当初始`@`后面没有一个字母（任何UNICODE字母），它从未被视为对自定义格式的引用。

- `time_zone`：用于格式化显示时间的时区名称。与所有设置一样，默认设置由程序员在设置FreeMarker（通过 `Configuration`类）时选择，但它通常是JVM的默认时区。可以是[Java TimeZone API](http://docs.oracle.com/javase/7/docs/api/java/util/TimeZone.html)接受的任何值，或者`"JVM default"`（自FreeMarker 2.3.21起）使用JVM默认时区。例如： `"GMT"`，`"GMT+2"`， `"GMT-1:30"`，`"CET"`， `"PST"`， `"America/Los_Angeles"`。

  警告！

  如果从默认值更改此设置，您当然也应设置 `sql_date_and_time_time_zone`为“JVM默认值”。请参阅Java API文档中的更多内容`Configurable.setSQLDateAndTimeTimeZone(TimeZone)`。

- `sql_date_and_time_time_zone` （因为FreeMarker 2.3.21）：这处理一个高度技术性的问题，所以通常应该由程序员从Java代码中设置。对于程序员：如果将其设置为非`null`，则对于仅来自SQL数据库的日期和仅限时间值（更准确地说，对于 `java.sql.Date`和`java.sql.Time`对象），FreeMarker将使用此时区而不是`time_zone`FreeMarker设置指定的时区 。请参阅Java API文档中的更多内容`Configurable.setSQLDateAndTimeTimeZone(TimeZone)`。

- `url_escaping_charset`：用于URL转义（例如，用于`${foo?url}`）计算转义（）部分的字符集 。通常，包含FreeMarker的框架应该设置它，因此您几乎不应该在模板中设置此设置。（程序员可以[在这里](https://freemarker.apache.org/docs/pgui_misc_charset.html)阅读更多相关信息 [......](https://freemarker.apache.org/docs/pgui_misc_charset.html)）`%*XX*`

- `output_encoding`：告诉FreeMarker输出的字符集是什么。由于FreeMarker输出UNICODE字符流（它写入a `java.io.Writer`），因此它不受输出编码的影响，但某些宏/函数和内置函数可能需要使用此信息。

- `classic_compatible`：用于与非常旧的FreeMarker版本（主要是1.7.x）更好地兼容。有关`freemarker.template.Configurable.isClassicCompatible()` 更多信息，请参阅文档 。

示例：假设模板的初始区域设置为 `de_DE`（德语）。然后这个：

```
$ {} 1.2
<#setting locale =“en_US”>
$ {} 1.2
```

将输出：

```
1,2
1.2
```

因为德国人使用逗号作为小数点分隔符，而美国人使用点。