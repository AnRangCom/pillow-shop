import { useParams, Link } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
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

export default function OrderDetailPage() {
  const { id } = useParams<{ id: string }>();

  const { data: order, isLoading } = useQuery({
    queryKey: ['order', id],
    queryFn: () => orderService.getById(Number(id)),
    enabled: !!id,
  });

  if (isLoading) return <div className="text-center py-8">Đang tải...</div>;
  if (!order) return <div className="text-center py-8">Không tìm thấy đơn hàng</div>;

  return (
    <div className="max-w-4xl mx-auto">
      <Link to="/orders" className="text-primary-600 hover:underline mb-4 inline-block">
        &larr; Quay lại danh sách
      </Link>

      <div className="bg-white rounded-lg shadow-md p-6 mb-6">
        <div className="flex justify-between items-start mb-4">
          <div>
            <h1 className="text-2xl font-bold">Đơn hàng {order.orderCode}</h1>
            <p className="text-gray-600">
              Ngày đặt: {new Date(order.orderDate).toLocaleDateString('vi-VN')}
            </p>
          </div>
          <span className={`px-4 py-2 rounded-full ${statusColors[order.status]}`}>
            {statusLabels[order.status]}
          </span>
        </div>

        <div className="border-t pt-4">
          <div className="flex justify-between items-center">
            <span className="text-lg font-semibold">Tổng tiền:</span>
            <span className="text-2xl font-bold text-primary-600">
              {order.totalAmount.toLocaleString('vi-VN')} VNĐ
            </span>
          </div>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-md p-6">
        <h2 className="text-xl font-bold mb-4">Trạng thái đơn hàng</h2>
        <div className="flex items-center justify-between">
          {(['PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED'] as OrderStatus[]).map((status, index) => {
            const isActive =
              ['PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED'].indexOf(order.status) >= index;
            return (
              <div key={status} className="flex flex-col items-center">
                <div
                  className={`w-10 h-10 rounded-full flex items-center justify-center ${
                    isActive ? 'bg-primary-600 text-white' : 'bg-gray-200 text-gray-400'
                  }`}
                >
                  {index + 1}
                </div>
                <span className="text-xs mt-2">{statusLabels[status]}</span>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
}
