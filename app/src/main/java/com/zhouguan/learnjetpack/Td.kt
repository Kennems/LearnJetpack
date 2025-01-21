package com.zhouguan.learnjetpack

class Td { // table data
    var content = ""
    fun html() = "\n\t<td>${content}</td>"
}

class Tr { // table rowd
    private val children = ArrayList<Td>()

    fun td(block: Td.() -> String) {
        val td = Td()
        td.content = td.block().toString()
        children.add(td)
    }

    fun html(): String {
        val builder = StringBuilder()
        builder.append("\n\t<tr>")
        for (childTag in children) {
            builder.append(childTag.html())
        }
        builder.append("\n\t</tr>")
        return builder.toString()
    }
}

class Table { // table
    private val children = ArrayList<Tr>()

    fun tr(block: Tr.() -> Unit) {
        val tr = Tr()
        tr.block()
        children.add(tr)
    }

    fun html(): String {
        val builder = StringBuilder()
        builder.append("<table>")
        for (childTag in children) {
            builder.append(childTag.html())

        }
        builder.append("\n</table>")
        return builder.toString()
    }
}

fun table(block: Table.() -> Unit): String {
    val table = Table()
    table.block()
    return table.html()
}

fun main() {
//    val html = table {
//        tr {
//            td(block = { "Apple" })
//            td { "Grape" }
//            td { "Orange" }
//        }
//        tr {
//            td { "Pear" }
//            td { "Banana" }
//            td { "Watermelon" }
//        }
//    }

    val html = table {
        repeat(10) {
            tr {
                val fruits = listOf("Apple", "Grape", "Orange")
                for (fruit in fruits) {
                    td { fruit }
                }
            }
        }
    }


    println(html)
}