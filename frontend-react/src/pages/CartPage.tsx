import { useCartStore } from '../utils/cart';
import { useAuthStore } from '../context/AuthContext';
import { orderService } from '../services/orderService';
import { useNavigate } from 'react-router-dom';
import toast from 'react-hot-toast';
import { useState } from 'react';

export default function CartPage() {
  const { items, removeItem, updateQuantity, clearCart, total } = useCartStore();
  const { user } = useAuthStore();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  const handleCheckout = async () => {
    if (items.length === 0) {
      toast.error('Giỏ hàng trống');
      return;
    }

    setLoading(true);
    try {
      const order = await orderService.create({
        status: 'PENDING',
        totalAmount: total,
      });

      for (const item of items) {
        await orderService.createItem({
          orderId: order.id,
          pillowId: item.pillowId,
          defaultSizeId: item.sizeId,
          quantity: item.quantity,
          sizeType: item.sizeId ? 'DEFAULT' : 'CUSTOM',
          price: item.basePrice,
        });
      }

      clearCart();
      toast.success('Đặt hàng thành công!');
      navigate('/orders');
    } catch (error: any) {
      toast.error(error.response?.data?.detail || 'Đặt hàng thất bại');
    } finally {
      setLoading(false);
    }
  };

  if (items.length === 0) {
    return (
      <div className="text-center py-16">
        <h2 className="text-2xl font-bold mb-4">Giỏ hàng trống</h2>
        <p className="text-gray-600">Hãy thêm sản phẩm vào giỏ hàng nhé!</p>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto">
      <h1 className="text-3xl font-bold mb-8">Giỏ hàng</h1>

      <div className="bg-white rounded-lg shadow-md p-6 mb-6">
        {items.map((item) => (
          <div key={`${item.pillowId}-${item.sizeId}`} className="flex items-center justify-between py-4 border-b">
            <div>
              <h3 className="font-semibold">{item.pillowName}</h3>
              <p className="text-gray-600 text-sm">
                {item.basePrice.toLocaleString('vi-VN')} VNĐ
              </p>
            </div>
            <div className="flex items-center gap-4">
              <div className="flex items-center gap-2">
                <button
                  onClick={() => updateQuantity(item.pillowId, item.sizeId, Math.max(1, item.quantity - 1))}
                  className="px-2 py-1 border rounded"
                >
                  -
                </button>
                <span className="w-8 text-center">{item.quantity}</span>
                <button
                  onClick={() => updateQuantity(item.pillowId, item.sizeId, item.quantity + 1)}
                  className="px-2 py-1 border rounded"
                >
                  +
                </button>
              </div>
              <span className="font-semibold w-32 text-right">
                {(item.basePrice * item.quantity).toLocaleString('vi-VN')} VNĐ
              </span>
              <button
                onClick={() => removeItem(item.pillowId, item.sizeId)}
                className="text-red-500 hover:text-red-700"
              >
                Xóa
              </button>
            </div>
          </div>
        ))}
      </div>

      <div className="bg-white rounded-lg shadow-md p-6">
        <div className="flex justify-between items-center mb-4">
          <span className="text-xl font-semibold">Tổng cộng:</span>
          <span className="text-2xl font-bold text-primary-600">
            {total.toLocaleString('vi-VN')} VNĐ
          </span>
        </div>
        <button
          onClick={handleCheckout}
          disabled={loading}
          className="w-full bg-primary-600 text-white py-3 rounded-lg hover:bg-primary-700 disabled:opacity-50"
        >
          {loading ? 'Đang xử lý...' : 'Đặt hàng'}
        </button>
      </div>
    </div>
  );
}
