package org.avr.notes.repo.postgresql

import org.jetbrains.exposed.sql.*

/**
 * Класс выражения для оператора ILIKE для поиска без учета регистра
 */
class ILikeOp(expr1: Expression<*>, expr2: Expression<*>) : ComparisonOp(expr1, expr2, "ILIKE")

/**
 * Функция для поиска по строковой колонке без учета регистра (ILIKE)
 */
infix fun<T: String?> ExpressionWithColumnType<T>.ilike(pattern: String): Op<Boolean> = ILikeOp(this, QueryParameter(pattern, columnType))