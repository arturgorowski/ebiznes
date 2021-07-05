
export interface LoginData {
    login: string;
    loginPassword: string;
}

export interface RegisterData {
    email: string;
    password: string;
}

export interface AuthenticationToken {
    id: number;
    email: string;
    loginData: {
        credentials: string;
    };
}
