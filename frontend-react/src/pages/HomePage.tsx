import { Link } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { pillowService } from '../services/pillowService';
import PillowCard from '../components/pillow/PillowCard';

export default function HomePage() {
  const { data, isLoading } = useQuery({
    queryKey: ['pillows', 'latest'],
    queryFn: () => pillowService.getAll(0, 6),
  });

  return (
    <div>
      <section className="text-center py-16 bg-gradient-to-r from-primary-500 to-primary-700 text-white rounded-2xl mb-12">
        <h1 className="text-4xl font-bold mb-4">Chào mừng đến với Pillow Shop</h1>
        <p className="text-xl mb-8">Tìm chiếc gối hoàn hảo cho giấc ngủ ngon</p>
        <Link
          to="/pillows"
          className="bg-white text-primary-600 px-8 py-3 rounded-full font-semibold hover:bg-gray-100"
        >
          Xem sản phẩm
        </Link>
      </section>

      <section>
        <h2 className="text-2xl font-bold mb-6">Sản phẩm mới nhất</h2>
        {isLoading ? (
          <div className="text-center py-8">Đang tải...</div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {data?.content.map((pillow) => (
              <PillowCard key={pillow.id} pillow={pillow} />
            ))}
          </div>
        )}
      </section>
    </div>
  );
}
