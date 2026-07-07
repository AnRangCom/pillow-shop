import { Link } from 'react-router-dom';
import type { Pillow } from '../../types';

interface PillowCardProps {
  pillow: Pillow;
}

export default function PillowCard({ pillow }: PillowCardProps) {
  return (
    <Link
      to={`/pillows/${pillow.id}`}
      className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow"
    >
      <div className="bg-gray-200 h-48 flex items-center justify-center">
        <span className="text-gray-400">Hình ảnh</span>
      </div>
      <div className="p-4">
        <h3 className="font-semibold text-lg mb-2">{pillow.name}</h3>
        {pillow.material && <p className="text-gray-600 text-sm mb-2">{pillow.material}</p>}
        <p className="text-primary-600 font-bold">
          {pillow.basePrice.toLocaleString('vi-VN')} VNĐ
        </p>
      </div>
    </Link>
  );
}
