package com.example.jvidiri.impostoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Impostometro extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impostometro);

        Button btn = (Button) findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculaImposto(v);
            }
        });
    }

    public void calculaImposto(View v) {
        TextView results = (TextView) findViewById(R.id.txt_Result);
        EditText salBrut = (EditText) findViewById(R.id.ed_SalBrut);
        //Pegando as variaveis que eu preciso
        double valorSalBruto = Double.parseDouble(salBrut.getText().toString());
        double valorSalSemInss;
        double aliquotaInsidente;
        double valorSalLiqui;

        if (valorSalBruto < 0) {
            results.setText("Por favor insira um salário válido.");
        } else {

            //Se o salário for maior que o teto nem faço conta.
            if (valorSalBruto >= 5189.82) {
                valorSalSemInss = valorSalBruto - 570.88;
                //return valorSalSemInss;
            } else {
                //tá temos que fazer a conta, pelo menos já sabemos que é maior que zero...
                aliquotaInsidente = 0.08;
                //probabilidade de o salário ser maior que a faixa do meio is too damm high....
                if (valorSalBruto >= 2594.93) {
                    aliquotaInsidente = 0.11;
                } else if (valorSalBruto >= 1556.95) {
                    //worst case for us o cara está na faixa do meio...
                    aliquotaInsidente = 0.09;
                }
                //Calculando o valor do salário descontado inss, baseado na Alíquota...
                valorSalSemInss = (valorSalBruto - (valorSalBruto * aliquotaInsidente));
                //return valorSalSemInss;
            }

            //Se não cair em uma das alicotas é sinal de que ele é isento.
            valorSalLiqui = valorSalSemInss;
            if ((valorSalSemInss >= 1903.99) && (valorSalSemInss <= 2826.65)) {
                //temos que calcular a segunda alicota e tirar o desconto...
                valorSalLiqui = valorSalSemInss - ((valorSalSemInss * 0.075) - 142.80);
            } else if ((valorSalSemInss >= 2826.66) && (valorSalSemInss <= 3751.05)) {
                valorSalLiqui = valorSalSemInss - ((valorSalSemInss * 0.15) - 354.80);
            } else if ((valorSalSemInss >= 3751.06) && (valorSalSemInss <= 4664.68)) {
                valorSalLiqui = valorSalSemInss - ((valorSalSemInss * 0.225) - 636.13);
            } else if (valorSalSemInss >= 4664.68) {
                valorSalLiqui = valorSalSemInss - ((valorSalSemInss * 0.275) - 869.36);
            }

            StringBuilder sb_result = new StringBuilder();
            sb_result.append("<strong>Salário Bruto: </strong>");
            sb_result.append(String.format("%.2f", valorSalBruto));
            sb_result.append("<br><strong>INSS devido: </strong>");
            sb_result.append(String.format("%.2f", valorSalBruto - valorSalSemInss));
            sb_result.append("<br><strong>Salário com desconto INSS: </strong>");
            sb_result.append(String.format("%.2f", valorSalSemInss));
            sb_result.append("<br><strong>IRRF devido: </strong>");
            sb_result.append(String.format("%.2f", valorSalSemInss - valorSalLiqui));
            sb_result.append("<br><strong>Salário Liquido: </strong>");
            sb_result.append(String.format("%.2f", valorSalLiqui));

            results.setText(Html.fromHtml(sb_result.toString()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_impostometro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle(R.string.action_settings);
            builder1.setMessage(R.string.calc_text);
            builder1.setCancelable(true);
            builder1.setNeutralButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
