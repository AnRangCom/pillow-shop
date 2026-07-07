import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { pillowService } from '../services/pillowService';
import PillowCard from '../components/pillow/PillowCard';

export default function PillowListPage() {
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState('');

  const { data, isLoading } = useQuery({
    queryKey: ['pillows', page, search],
    queryFn: () => (search ? pillowService.search(search, undefined, undefined, page) : pillowService.getAll(page)),
  });

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8">Danh sách sản phẩm</h1>

      <div className="mb-6">
        <input
          type="text"
          placeholder="Tìm kiếm gối..."
          value={search}
          onChange={(e) => {
            setSearch(e.target.value);
            setPage(0);
          }}
          className="w-full md:w-96 px-4 py-2 border rounded-lg focus:outline-none focus:border-primary-500"
        />
      </div>

      {isLoading ? (
        <div className="text-center py-8">Đang tải...</div>
      ) : (
        <>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {data?.content.map((pillow) => (
              <PillowCard key={pillow.id} pillow={pillow} />
            ))}
          </div>

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
        </>
      )}
    </div>
  );
}
