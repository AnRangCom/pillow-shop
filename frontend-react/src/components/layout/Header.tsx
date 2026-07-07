import { Link, useNavigate } from 'react-router-dom';
import { useAuthStore } from '../../context/AuthContext';
import { FaShoppingCart, FaUser, FaSignOutAlt } from 'react-icons/fa';

export default function Header() {
  const { token, user, logout } = useAuthStore();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="bg-white shadow-md">
      <div className="container mx-auto px-4 py-4 flex items-center justify-between">
        <Link to="/" className="text-2xl font-bold text-primary-600">
          Pillow Shop
        </Link>

        <nav className="flex items-center gap-6">
          <Link to="/pillows" className="text-gray-600 hover:text-primary-600">
            Sản phẩm
          </Link>

          {token ? (
            <>
              <Link to="/cart" className="text-gray-600 hover:text-primary-600">
                <FaShoppingCart className="inline mr-1" /> Giỏ hàng
              </Link>
              <Link to="/orders" className="text-gray-600 hover:text-primary-600">
                Đơn hàng
              </Link>
              <div className="flex items-center gap-2">
                <FaUser className="text-gray-500" />
                <span className="text-sm text-gray-600">{user?.login || 'User'}</span>
                <button onClick={handleLogout} className="text-red-500 hover:text-red-700">
                  <FaSignOutAlt />
                </button>
              </div>
            </>
          ) : (
            <>
              <Link to="/login" className="text-gray-600 hover:text-primary-600">
                Đăng nhập
              </Link>
              <Link
                to="/register"
                className="bg-primary-600 text-white px-4 py-2 rounded hover:bg-primary-700"
              >
                Đăng ký
              </Link>
            </>
          )}
        </nav>
      </div>
    </header>
  );
}
