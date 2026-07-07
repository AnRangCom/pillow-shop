import { create } from 'zustand';

export interface CartItem {
  pillowId: number;
  pillowName: string;
  basePrice: number;
  sizeId?: number;
  quantity: number;
}

interface CartState {
  items: CartItem[];
  total: number;
  addToCart: (item: CartItem) => void;
  removeItem: (pillowId: number, sizeId?: number) => void;
  updateQuantity: (pillowId: number, sizeId: number | undefined, quantity: number) => void;
  clearCart: () => void;
}

export const useCartStore = create<CartState>((set, get) => ({
  items: [],
  total: 0,

  addToCart: (item: CartItem) => {
    set((state) => {
      const existing = state.items.find(
        (i) => i.pillowId === item.pillowId && i.sizeId === item.sizeId
      );

      if (existing) {
        const updated = state.items.map((i) =>
          i.pillowId === item.pillowId && i.sizeId === item.sizeId
            ? { ...i, quantity: i.quantity + item.quantity }
            : i
        );
        return {
          items: updated,
          total: updated.reduce((sum, i) => sum + i.basePrice * i.quantity, 0),
        };
      }

      const newItems = [...state.items, item];
      return {
        items: newItems,
        total: newItems.reduce((sum, i) => sum + i.basePrice * i.quantity, 0),
      };
    });
  },

  removeItem: (pillowId: number, sizeId?: number) => {
    set((state) => {
      const newItems = state.items.filter(
        (i) => !(i.pillowId === pillowId && i.sizeId === sizeId)
      );
      return {
        items: newItems,
        total: newItems.reduce((sum, i) => sum + i.basePrice * i.quantity, 0),
      };
    });
  },

  updateQuantity: (pillowId: number, sizeId: number | undefined, quantity: number) => {
    set((state) => {
      const newItems = state.items.map((i) =>
        i.pillowId === pillowId && i.sizeId === sizeId ? { ...i, quantity } : i
      );
      return {
        items: newItems,
        total: newItems.reduce((sum, i) => sum + i.basePrice * i.quantity, 0),
      };
    });
  },

  clearCart: () => set({ items: [], total: 0 }),
}));
