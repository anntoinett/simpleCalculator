package com.example.antonia_pekala_sr_12_30

interface CalculatorBrainInterface {
    public fun addNumber(value: String)
    public fun addOperation(value: String)
    public fun evaluateExpression():Double
    public fun squareRoot():Double
    public fun removeLastOperation()
    public fun clear()
}