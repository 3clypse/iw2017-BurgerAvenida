package com.pd.model;

public enum IVA {
	TYPE1(.1f), TYPE2(.21f);
	
	private final double iva;

    private IVA(double iva) {
        this.iva = iva;
    }

    public double getIVA() {
        return iva;
    }
}
