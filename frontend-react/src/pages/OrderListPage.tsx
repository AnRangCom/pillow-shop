import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import { orderService } from '../services/orderService';
import type { OrderStatus } from '../types';

const statusColors: Record<OrderStatus, string> = {
  PENDING: 'bg-yellow-100 text-yellow-800',
  CONFIRMED: 'bg-blue-100 text-blue-800',
  SHIPPED: 'bg-purple-100 text-purple-800',
  DELIVERED: 'bg-green-100 text-green-800',
  CANCELLED: 'bg-red-100 text-red-800',
};

const statusLabels: Record<OrderStatus, string> = {
  PENDING: 'Chờ xác nhận',
  CONFIRMED: 'Đã xác nhận',
  SHIPPED: 'Đang giao',
  DELIVERED: 'Đã giao',
  CANCELLED: 'Đã hủy',
};

export default function OrderListPage() {
  const [page, setPage] = useState(0);

  const { data, isLoading } = useQuery({
    queryKey: ['orders', page],
    queryFn: () => orderService.getAll(page),
  });

  if (isLoading) return <div className="text-center py-8">Đang tải...</div>;

  return (
    <div className="max-w-4xl mx-auto">
      <h1 className="text-3xl font-bold mb-8">Đơn hàng của tôi</h1>

      {data?.content.length === 0 ? (
        <div className="text-center py-16">
          <p className="text-gray-600 mb-4">Bạn chưa có đơn hàng nào</p>
          <Link to="/pillows" className="text-primary-600 hover:underline">
            Mua sắm ngay
          </Link>
        </div>
      ) : (
        <div className="space-y-4">
          {data?.content.map((order) => (
            <Link
              key={order.id}
              to={`/orders/${order.id}`}
              className="block bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow"
            >
              <div className="flex justify-between items-start">
                <div>
                  <p className="font-semibold">Mã đơn: {order.orderCode}</p>
                  <p className="text-gray-600 text-sm">
                    {new Date(order.orderDate).toLocaleDateString('vi-VN')}
                  </p>
                </div>
                <div className="text-right">
                  <span className={`px-3 py-1 rounded-full text-sm ${statusColors[order.status]}`}>
                    {statusLabels[order.status]}
                  </span>
                  <p className="font-bold text-primary-600 mt-2">
                    {order.totalAmount.toLocaleString('vi-VN')} VNĐ
                  </p>
                </div>
              </div>
            </Link>
          ))}

          {data && data.totalPages > 1 && (
            <div className="flex justify-center gap-2 mt-8">
              <button
                onClick={() => setPage((p) => Math.max(0, p - 1))}
                disabled={page === 0}
                className="px-4 py-2 border rounded disabled:opacity-50"
              >
                Trước
              </button>
              <span className="px-4 py-2">
                {page + 1} / {data.totalPages}
              </span>
              <button
                onClick={() => setPage((p) => Math.min(data.totalPages - 1, p + 1))}
                disabled={page >= data.totalPages - 1}
                className="px-4 py-2 border rounded disabled:opacity-50"
              >
                Sau
              </button>
            </div>
          )}
        </div>
      )}
    </div>
  );
}
