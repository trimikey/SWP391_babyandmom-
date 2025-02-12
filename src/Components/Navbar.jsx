import React from "react";

const Navbar = () => {
  return (
    <nav className="flex justify-between items-center p-4 shadow-md">
      <div className="text-pink-600 font-bold text-lg">Baby and Mom</div>
      <ul className="flex gap-4">
        <li><a href="#" className="hover:text-pink-600">Trang chủ</a></li>
        <li><a href="#" className="hover:text-pink-600">Dịch vụ</a></li>
        <li><a href="#" className="hover:text-pink-600">Blog</a></li>
        <li><a href="#" className="hover:text-pink-600">FAQ</a></li>
        <li><a href="#" className="hover:text-pink-600">Dashboard</a></li>
      </ul>
      <div>
        <button className="px-4 py-2 bg-pink-500 text-white rounded">Login</button>
        <button className="ml-2 px-4 py-2 border rounded">Sign Up</button>
      </div>
    </nav>
  );
};

export default Navbar;
