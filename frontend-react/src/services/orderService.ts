import api, { ENDPOINTS } from '../config/api';
import type { CustomerOrder, OrderItem, OrderStatus, PageResponse } from '../types';

export const orderService = {
  getAll: async (page = 0, size = 10): Promise<PageResponse<CustomerOrder>> => {
    const response = await api.get(ENDPOINTS.ORDERS, { params: { page, size, sort: 'id,desc' } });
    return response.data;
  },

  getById: async (id: number): Promise<CustomerOrder> => {
    const response = await api.get(`${ENDPOINTS.ORDERS}/${id}`);
    return response.data;
  },

  getByStatus: async (status: OrderStatus, page = 0, size = 10): Promise<PageResponse<CustomerOrder>> => {
    const response = await api.get(ENDPOINTS.ORDERS, { params: { status, page, size } });
    return response.data;
  },

  create: async (order: Partial<CustomerOrder>): Promise<CustomerOrder> => {
    const response = await api.post(ENDPOINTS.ORDERS, order);
    return response.data;
  },

  updateStatus: async (id: number, status: OrderStatus): Promise<CustomerOrder> => {
    const response = await api.patch(`${ENDPOINTS.ORDERS}/${id}/status`, null, { params: { status } });
    return response.data;
  },

  createItem: async (item: Partial<OrderItem>): Promise<OrderItem> => {
    const response = await api.post(ENDPOINTS.ORDER_ITEMS, item);
    return response.data;
  },

  getItemsByOrder: async (orderId: number): Promise<OrderItem[]> => {
    const response = await api.get(ENDPOINTS.ORDER_ITEMS, { params: { orderId } });
    return response.data.content;
  },
};
