package edu.tcu.tanmaykejriwal.calculator2

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.math.exp


//global vars for input handling
var hasDecimal=false
var newNumber = true
var notNumberYet = true


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val str = StringBuilder()
        str.append("0")

        //Number Buttons
        val button0 = findViewById<Button>(R.id.button0)
        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)
        val button4 = findViewById<Button>(R.id.button4)
        val button5 = findViewById<Button>(R.id.button5)
        val button6 = findViewById<Button>(R.id.button6)
        val button7 = findViewById<Button>(R.id.button7)
        val button8 = findViewById<Button>(R.id.button8)
        val button9 = findViewById<Button>(R.id.button9)

        button0.setOnClickListener{
            evaluateExpression(str,"0")
        }

        button1.setOnClickListener{
            evaluateExpression(str,"1")
        }
        button2.setOnClickListener{
            evaluateExpression(str,"2")
        }
        button3.setOnClickListener{
            evaluateExpression(str,"3")
        }
        button4.setOnClickListener{
            evaluateExpression(str,"4")
        }
        button5.setOnClickListener{
            evaluateExpression(str,"5")
        }
        button6.setOnClickListener{
            evaluateExpression(str,"6")
        }
        button7.setOnClickListener{
            evaluateExpression(str,"7")
        }
        button8.setOnClickListener{
            evaluateExpression(str,"8")
        }
        button9.setOnClickListener{
            evaluateExpression(str,"9")
        }


        //Operators
        val divide = findViewById<Button>(R.id.btn_div)
        val multiply = findViewById<Button>(R.id.btn_mult)
        val subtract = findViewById<Button>(R.id.btn_sub)
        val add = findViewById<Button>(R.id.btn_add)
        val clearbtn = findViewById<Button>(R.id.btn_clr)
        val equal = findViewById<Button>(R.id.btn_eq)
        val decimal = findViewById<Button>(R.id.btn_decimal)
        val Result = findViewById<TextView>(R.id.result_tv)

        add.setOnClickListener {
            evaluateExpression(str,"+")
        }
        subtract.setOnClickListener {
            evaluateExpression(str,"-")
        }
        multiply.setOnClickListener {
            evaluateExpression(str,"*")
        }
        divide.setOnClickListener {
            evaluateExpression(str,"/")
        }
        decimal.setOnClickListener {
            evaluateExpression(str,".")
        }

        clearbtn.setOnClickListener{
            str.clear()
            str.append("0")
            Result.text= str.toString()
            hasDecimal=false
            newNumber = true
            notNumberYet = true
        }

        equal.setOnClickListener {
            val ans = calculate2(str.toString())
            Result.text= ans
            str.clear()
            str.append(ans)
        }



    }

    //input validation
    fun evaluateExpression(curr:java.lang.StringBuilder, expr: String) {
        val Result = findViewById<TextView>(R.id.result_tv)
        val temp = expr.toCharArray()
        val seta = setOf('/','*', '-','+')
        curr.append(expr)


        //handling multiple decimal points in a single number
        if(hasDecimal && expr=="."){
            println(2)
            curr.deleteCharAt(curr.length-1)
        }
        if(expr=="."){
            println(2)
            hasDecimal = true
        }

        if(expr=="/" || expr == "*" || expr == "-" || expr == "+"){
            println(3)
            hasDecimal=false
            newNumber = true
            notNumberYet = true
        }

        if(temp[0].isDigit() && temp[0]!='0'){
            println(4)
            notNumberYet = false
        }

        //handling multiple operators

        if(curr.length>1){
            val ch: Char = curr[curr.length-2]
            if(seta.contains(temp[0]) &&  seta.contains(ch))
            {
                curr.deleteCharAt(curr.length-1)
            }
        }
        //handling the firstDigit Zero exception
        if(curr.length>1){

            val ch: Char = curr[curr.length-2]
            val temp = expr.toCharArray()
            if(temp[0] =='0' && ch=='0' && notNumberYet && hasDecimal==false)
            {
                println(5)
                curr.deleteCharAt(curr.length-1)
            }
        }


        Result.text = curr.toString()
    }

    fun calculate2(s:String):String{
        val ch = s.toCharArray()
        val seta = setOf('/','*', '-','+','.')
        if(seta.contains(ch[ch.size-1])){
            return s;
        }
        val chars = s.toCharArray()
        val stackNumber = Stack<BigDecimal>()
        var stackOps = Stack<Char>()
        val ans = Stack<BigDecimal>()
        val temp = StringBuilder()
        var i = 0
        println(ch)
        while (i < chars.size) {
            val letter = chars[i]
            if (letter.isDigit() || letter == '.'){
                temp.append(letter)
            }
            else{
                if(temp.length > 0){
                    stackNumber.push(BigDecimal(temp.toString()))
                }
                stackOps.push(letter)
                temp.clear()
            }
            i++;
        }

        //adding the last number
        stackNumber.push(BigDecimal(temp.toString()))



        var j = 0;
        for (i in 0 until stackNumber.size) {
            var num = stackNumber[i]
            if(i==0){
                //handling if the first number becomes negative
                if(stackOps.size==stackNumber.size){
                    ans.push(BigDecimal("-1").multiply(num))
                    j++
                    continue
                }
                ans.push(num)
                continue
            }
            var sign = stackOps[j]
            when (sign) {
                null -> ans.push(num)
                '+' -> ans.push(num)
                '-' -> ans.push(BigDecimal("-1").multiply(num))
                '*' -> ans.push(num.multiply(ans.pop()))
                '/' -> if(num.compareTo(BigDecimal.ZERO) == 0){
                    return "Error! Tap CLR to continue"
                }else{ans.push(ans.pop().divide(num,8, RoundingMode.HALF_UP))}
            }
            j++;
            println(ans)
        }


        var ansv = BigDecimal("0");
        //adding up the whole stack
        for(numbs in ans)
        {
            ansv = ansv.add(numbs)
        }

        //removing extra zeroes and returning the value
        return ansv.stripTrailingZeros().toPlainString()

    }



}