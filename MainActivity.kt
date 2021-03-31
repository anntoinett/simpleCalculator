package com.example.antonia_pekala_sr_12_30



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView


class MainActivity : AppCompatActivity() {
    internal lateinit var clearButton: Button
    internal lateinit var equalButton: Button
    internal lateinit var commaButton: Button
    internal lateinit var sqrtButton: Button

    internal lateinit var digits: Array<Button>
    internal lateinit var operations: Array<Button>

    internal lateinit var calcDisplay: TextView

    internal val calculatorEngine: CalculatorBrainInterface = CalculatorBrain()

    val OPERATORS = mutableListOf<String>()

    private var commaUsed: Boolean = false
    private var lastNumeric = true
    private var clearUsed = true
    private var showResult = false
    private var lastSymbol = ""

    //dwie zmienne z setterem do uaktualniania widoku
    private var lastNumber = ""
    set(value){
        if(value == "" && clearUsed){
            setDisplay("0")
            clearUsed =false
        }
        else if(calcDisplay.text.toString() == "0") {
            setDisplay(value)
            lastSymbol = value
            clearUsed = false
        }
        else if(value != ""){
            if(showResult or (value == "0.")){
                setDisplay(calcDisplay.text.toString() +value)
                showResult = false
            }
            else setDisplay(calcDisplay.text.toString() +value.last())
            lastSymbol = value
        }
        field =value
    }

    private var lastOperator = ""
        set(value) {
            if (value != "") {
                setDisplay(calcDisplay.text.toString() + value)
                lastSymbol = value

            }
            field = value
        }



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val digitsIds = arrayOf(R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9)
        val operationsIds = arrayOf(R.id.buttonAdd, R.id.buttonSub, R.id.buttonDiv, R.id.buttonMul, R.id.buttonPercent)
        sqrtButton = findViewById(R.id.buttonSqrt)
        OpSymbols.addSymbol = getString(R.string.add)
        OPERATORS.add(OpSymbols.addSymbol)
        OpSymbols.subSymbol = getString(R.string.substract)
        OPERATORS.add(OpSymbols.subSymbol)
        OpSymbols.mulSymbol = getString(R.string.multiply)
        OPERATORS.add(OpSymbols.mulSymbol)
        OpSymbols.divSymbol = getString(R.string.divide)
        OPERATORS.add(OpSymbols.divSymbol)
        OpSymbols.perSymbol = getString(R.string.percent)
        OPERATORS.add(OpSymbols.perSymbol)
        OpSymbols.sqSymbol = getString(R.string.sqrt)

        digits = (digitsIds.map {id -> findViewById(id) as Button}).toTypedArray()
        operations = (operationsIds.map{id -> findViewById(id) as Button}).toTypedArray()

        clearButton = findViewById(R.id.buttonClear)
        commaButton = findViewById(R.id.buttonComma)
        equalButton = findViewById(R.id.buttonEq)

        calcDisplay = findViewById(R.id.calculatorDisplay)

        lastNumber = "0"

        clearButton.setOnClickListener{clear()}
        commaButton.setOnClickListener{commaPressed()}
        equalButton.setOnClickListener{evaluateFormula()}
        digits.forEach { button -> button.setOnClickListener{i -> buttonPressed(i as Button)} }
        operations.forEach { button -> button.setOnClickListener{i -> operationPressed(i as Button)} }
        sqrtButton.setOnClickListener{button -> sqrtPressed(button as Button)}
    }

    private fun sqrtPressed(button: Button) {
/*
        println("sqrt pressed")
*/
        if(lastNumber.isEmpty()) return
        if(lastNumber.last() == '.'){
            lastNumber += "0"
        }
        if(lastSymbol in OPERATORS){
            lastSymbol=""
            calculatorEngine.removeLastOperation()
        }
        calculatorEngine.addNumber(lastNumber)

        val result = calculatorEngine.squareRoot()
        calcDisplay.text = ""
        showResult =true
        lastNumber = result.toString()
        lastNumeric = true
        commaUsed = true
    }
    //klasa przechowująca zdefiniowane w strings.xml operatory, defaultowo jak poniżej
    class OpSymbols {
        companion object {
            var addSymbol = "+"
            var subSymbol = "-"
            var mulSymbol = "*"
            var divSymbol = "/"
            var perSymbol = "%"
            var sqSymbol = "√"
        }
    }

    private fun operationPressed(operation: Button) {
/*
        println("Pressed operation: " + operation.text)
*/
            if(lastNumeric){
                calculatorEngine.addOperation(operation.text.toString())
                lastNumeric = false
                lastOperator = operation.text.toString()
            }else{
                calcDisplay.text = calcDisplay.text.toString().dropLast(1)
                calculatorEngine.removeLastOperation()
                calculatorEngine.addOperation(operation.text.toString())
                lastOperator = operation.text.toString()
            }
        lastNumeric = false
        commaUsed = false
    }

    private fun buttonPressed(digit: Button) {
/*
        println("Pressed digit: " + digit.text)
*/

        if(lastNumeric){
            if(lastNumber == "0"){
                lastNumber = ""
                lastNumber = digit.text.toString()
            }else{
                lastNumber += digit.text.toString()
            }
        }else{
            calculatorEngine.addNumber(lastNumber)
            lastNumeric = true
            lastNumber = ""
            lastNumber = digit.text.toString()
        }
    }

    private fun evaluateFormula() {
/*
        println("= button pressed")
*/
        if(lastNumber.isEmpty()) return
        if(lastNumber.last() == '.'){
            lastNumber += "0"
        }
        if(lastSymbol in OPERATORS){
            lastSymbol=""
            calculatorEngine.removeLastOperation()
        }
        calculatorEngine.addNumber(lastNumber)

        val result = calculatorEngine.evaluateExpression()
        calcDisplay.text = ""
        showResult =true
        lastNumber = result.toString()
        lastNumeric = true
        commaUsed = true


    }

    private fun commaPressed() {
/*
        println(". pressed")
*/

        if(!commaUsed){
            commaUsed = true
            if(!lastNumeric){
                calculatorEngine.addNumber(lastNumber)
                lastNumeric = true
                lastNumber = ""
                lastNumber += "0."
            }
            else lastNumber += "."
            lastNumeric = true

        } else if(lastNumber == ""){
            commaUsed = true
            lastNumber+="0."
            lastNumeric = true
        }
    }

    private fun clear(){
        calculatorEngine.clear()
        clearUsed = true
        lastNumeric = true
        commaUsed = false
        lastNumber=""
        lastNumber = "0"
        lastSymbol = ""
        lastOperator = ""

/*
        println("Clear button pressed")
*/

    }

    private fun setDisplay(value: String){
        calcDisplay.text = ""
        calcDisplay.append(value)

    }
}
