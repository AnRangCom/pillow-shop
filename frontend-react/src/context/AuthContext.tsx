import { create } from 'zustand';
import { authService } from '../services/authService';

interface AuthState {
  token: string | null;
  user: any | null;
  login: (username: string, password: string) => Promise<void>;
  register: (data: any) => Promise<void>;
  logout: () => void;
  loadUser: () => Promise<void>;
}

export const useAuthStore = create<AuthState>((set) => ({
  token: localStorage.getItem('token'),
  user: null,

  login: async (username: string, password: string) => {
    const response = await authService.login({ username, password });
    localStorage.setItem('token', response.id_token);
    set({ token: response.id_token });
  },

  register: async (data: any) => {
    await authService.register(data);
  },

  logout: () => {
    localStorage.removeItem('token');
    set({ token: null, user: null });
  },

  loadUser: async () => {
    try {
      const user = await authService.getAccount();
      set({ user });
    } catch {
      set({ token: null, user: null });
      localStorage.removeItem('token');
    }
  },
}));
