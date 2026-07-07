import api, { ENDPOINTS } from '../config/api';
import type { Pillow, DefaultSize, PageResponse } from '../types';

export const pillowService = {
  getAll: async (page = 0, size = 12): Promise<PageResponse<Pillow>> => {
    const response = await api.get(ENDPOINTS.PILLOWS, { params: { page, size, sort: 'id,desc' } });
    return response.data;
  },

  getById: async (id: number): Promise<Pillow> => {
    const response = await api.get(`${ENDPOINTS.PILLOWS}/${id}`);
    return response.data;
  },

  search: async (name?: string, minPrice?: number, maxPrice?: number, page = 0, size = 12): Promise<PageResponse<Pillow>> => {
    const params: any = { page, size, sort: 'id,desc' };
    if (name) params.name = name;
    if (minPrice !== undefined) params.minPrice = minPrice;
    if (maxPrice !== undefined) params.maxPrice = maxPrice;
    const response = await api.get(ENDPOINTS.PILLOWS, { params });
    return response.data;
  },

  getSizes: async (pillowId: number): Promise<DefaultSize[]> => {
    const response = await api.get(ENDPOINTS.DEFAULT_SIZES, { params: { pillowId } });
    return response.data.content;
  },
};
