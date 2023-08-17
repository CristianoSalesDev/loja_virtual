package com.br.lojavirtual;

import com.br.lojavirtual.util.ValidaCPF;
import com.br.lojavirtual.util.ValidaCnpj;

public class TesteCPFCNPJ {

	public static void main(String[] args) {
		
		boolean isCnpj = ValidaCnpj.isCNPJ("26.110.201/0001-35");
		
		boolean isCpf = ValidaCPF.isCPF("592.090.180-28");
		
		System.out.println("Cnpj Válido: " + isCnpj);
		
		System.out.println("Cpf Válido: " + isCpf);
	}
}
