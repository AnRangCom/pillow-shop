import { useParams } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { pillowService } from '../services/pillowService';
import { useState } from 'react';
import toast from 'react-hot-toast';
import { useCartStore } from '../utils/cart';
import { useAuthStore } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

export default function PillowDetailPage() {
  const { id } = useParams<{ id: string }>();
  const [selectedSize, setSelectedSize] = useState<number | null>(null);
  const [quantity, setQuantity] = useState(1);
  const { addToCart } = useCartStore();
  const { token } = useAuthStore();
  const navigate = useNavigate();

  const { data: pillow, isLoading } = useQuery({
    queryKey: ['pillow', id],
    queryFn: () => pillowService.getById(Number(id)),
    enabled: !!id,
  });

  const { data: sizes } = useQuery({
    queryKey: ['sizes', id],
    queryFn: () => pillowService.getSizes(Number(id)),
    enabled: !!id,
  });

  const handleAddToCart = () => {
    if (!token) {
      toast.error('Vui lòng đăng nhập để thêm vào giỏ hàng');
      navigate('/login');
      return;
    }
    if (pillow) {
      addToCart({
        pillowId: pillow.id,
        pillowName: pillow.name,
        basePrice: pillow.basePrice,
        sizeId: selectedSize || undefined,
        quantity,
      });
      toast.success('Đã thêm vào giỏ hàng!');
    }
  };

  if (isLoading) return <div className="text-center py-8">Đang tải...</div>;
  if (!pillow) return <div className="text-center py-8">Không tìm thấy sản phẩm</div>;

  return (
    <div className="max-w-4xl mx-auto">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        <div className="bg-gray-200 h-96 rounded-lg flex items-center justify-center">
          <span className="text-gray-400 text-lg">Hình ảnh sản phẩm</span>
        </div>

        <div>
          <h1 className="text-3xl font-bold mb-4">{pillow.name}</h1>
          <p className="text-2xl text-primary-600 font-semibold mb-4">
            {pillow.basePrice.toLocaleString('vi-VN')} VNĐ
          </p>
          {pillow.material && (
            <p className="text-gray-600 mb-2">
              <strong>Chất liệu:</strong> {pillow.material}
            </p>
          )}
          {pillow.description && <p className="text-gray-600 mb-6">{pillow.description}</p>}

          {sizes && sizes.length > 0 && (
            <div className="mb-6">
              <h3 className="font-semibold mb-2">Kích thước:</h3>
              <div className="flex flex-wrap gap-2">
                {sizes.map((size) => (
                  <button
                    key={size.id}
                    onClick={() => setSelectedSize(size.id)}
                    className={`px-4 py-2 border rounded ${
                      selectedSize === size.id ? 'border-primary-600 bg-primary-50' : 'border-gray-300'
                    }`}
                  >
                    {size.name} ({size.length}x{size.width})
                    {size.extraPrice > 0 && <span className="text-sm text-gray-500 ml-1">+{size.extraPrice.toLocaleString('vi-VN')}đ</span>}
                  </button>
                ))}
              </div>
            </div>
          )}

          <div className="mb-6">
            <h3 className="font-semibold mb-2">Số lượng:</h3>
            <div className="flex items-center gap-2">
              <button
                onClick={() => setQuantity(Math.max(1, quantity - 1))}
                className="px-3 py-1 border rounded"
              >
                -
              </button>
              <span className="w-12 text-center">{quantity}</span>
              <button onClick={() => setQuantity(quantity + 1)} className="px-3 py-1 border rounded">
                +
              </button>
            </div>
          </div>

          <button
            onClick={handleAddToCart}
            className="w-full bg-primary-600 text-white py-3 rounded-lg hover:bg-primary-700"
          >
            Thêm vào giỏ hàng
          </button>
        </div>
      </div>
    </div>
  );
}
