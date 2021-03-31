package com.example.antonia_pekala_sr_12_30

import android.widget.Button
import kotlin.math.sqrt

public class CalculatorBrain : CalculatorBrainInterface{
    var numbers = mutableListOf<Double>()
    var operations = mutableListOf<String>()

    override fun addNumber(value: String){
        val num = value.toDouble()
        numbers.add(num)
    }
    override fun addOperation(value: String){
        operations.add(value)
    }

    @ExperimentalStdlibApi
    override fun evaluateExpression():Double{
        //obsługa kolejności działań przy występujących znakach o wyższym priorytecie
        val highOperators = arrayOf(MainActivity.OpSymbols.divSymbol, MainActivity.OpSymbols.mulSymbol, MainActivity.OpSymbols.perSymbol)
        if(numbers.size < 2 != true && operations.isEmpty() != true && operations.any(){it in highOperators}){
            /*println(numbers.toString())
            println(operations.toString())*/
            var toDelete = mutableListOf<Int>()
            for((i,op) in operations.withIndex()){
                if(!(op in highOperators)) continue
                else{
                    toDelete.add(i)
                    var leftNum = numbers.get(i)
                    var rightNum = numbers.get(i+1)
                    numbers.set(i+1,eval(leftNum, rightNum, operations.get(i)))
                }
            }

            var toSubstract = 0
            for (i in toDelete){
                numbers.removeAt(i-toSubstract)
                operations.removeAt(i-toSubstract)
                toSubstract+=1
            }

            /*println(numbers.toString())
            println(operations.toString())*/
        }

        //pozostałe obliczenia od lewej do prawej
        while(numbers.size < 2 != true && operations.isEmpty() != true) {
            /*println(numbers.toString())
            println(operations.toString())*/
            numbers.add(0,eval(numbers.removeFirst(), numbers.removeFirst(), operations.removeFirst()))
        }
        /*println(numbers.toString())
        println(operations.toString())*/

        var numbers_copy = numbers.toList()
        clear()
        if(!numbers_copy.isEmpty()) return numbers_copy.toMutableList().removeFirst()
        else return 0.0

    }
    //wykonanie operacji
    private fun eval(a:Double, b: Double, op: String):Double{
        /*println("a: "+a.toString() + " b: " + b.toString())
        println("Operacja: " + op)
        println(R.string.add.toString())*/
        when (op){
            MainActivity.OpSymbols.addSymbol-> return roundTo4(a+b)
            MainActivity.OpSymbols.mulSymbol-> return roundTo4(a*b)
            MainActivity.OpSymbols.divSymbol-> return roundTo4(a/b)
            MainActivity.OpSymbols.subSymbol -> return roundTo4(a-b)
            MainActivity.OpSymbols.perSymbol -> return roundTo4((a/100)*b)
        }
        return 0.0
    }

    @ExperimentalStdlibApi
    override fun removeLastOperation() {
        if (!operations.isEmpty()) {
            operations.removeFirst()
        }
    }

    override fun clear(){
        numbers.clear()
        operations.clear()
    }
    //zaokrąglenie do 4 miejsc po przecinku
    private fun roundTo4(num: Double):Double{
        return Math.round(num*10000.0)/10000.0
    }

    @kotlin.ExperimentalStdlibApi
    override fun squareRoot():Double {
        return roundTo4(sqrt(evaluateExpression()))
    }

}