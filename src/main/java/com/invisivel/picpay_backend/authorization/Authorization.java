package com.invisivel.picpay_backend.authorization;

public record Authorization(
    String status, // Campo "status"
    Data data      // Objeto "data"
) {
    public boolean isAuthorization() {
        return data != null && data.authorization(); // Verifica o valor de "authorization"
    }

    // Classe interna representando o objeto "data"
    public static record Data(boolean authorization) {
    }


}
