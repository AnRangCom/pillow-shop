export interface Pillow {
  id: number;
  name: string;
  description?: string;
  material?: string;
  basePrice: number;
}

export interface DefaultSize {
  id: number;
  name: string;
  length: number;
  width: number;
  extraPrice: number;
  pillowId: number;
}

export interface Customer {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  address?: string;
  userId: number;
}

export interface CustomerOrder {
  id: number;
  orderCode: string;
  orderDate: string;
  status: OrderStatus;
  totalAmount: number;
  customerId: number;
  customerName?: string;
}

export interface OrderItem {
  id: number;
  quantity: number;
  sizeType: SizeType;
  customLength?: number;
  customWidth?: number;
  price: number;
  pillowId: number;
  pillowName?: string;
  defaultSizeId?: number;
  defaultSizeName?: string;
  orderId: number;
}

export type OrderStatus = 'PENDING' | 'CONFIRMED' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED';
export type SizeType = 'DEFAULT' | 'CUSTOM';

export interface User {
  id: number;
  login: string;
  firstName?: string;
  lastName?: string;
  email?: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
