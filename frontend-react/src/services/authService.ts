import api, { ENDPOINTS } from '../config/api';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  id_token: string;
}

export interface RegisterRequest {
  login: string;
  email: string;
  password: string;
  firstName?: string;
  lastName?: string;
}

export const authService = {
  login: async (data: LoginRequest): Promise<LoginResponse> => {
    const response = await api.post(ENDPOINTS.AUTH.LOGIN, data);
    return response.data;
  },

  register: async (data: RegisterRequest): Promise<void> => {
    await api.post(ENDPOINTS.AUTH.REGISTER, data);
  },

  getAccount: async () => {
    const response = await api.get(ENDPOINTS.AUTH.ACCOUNT);
    return response.data;
  },
};
