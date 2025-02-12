import React from "react";

const HeroSection = () => {
  return (
    <div className="bg-pink-100 p-10 text-center">
      <h1 className="text-3xl font-bold">Trung Tâm Baby And Mom</h1>
      <p className="text-lg text-gray-600">Hành trình thai kỳ của mẹ, niềm vui của bé</p>
      <p className="text-sm text-gray-500">Theo dõi sự phát triển của thai nhi và nhận thông tin khoa học</p>
      <div className="mt-4">
        <button className="px-6 py-3 bg-pink-500 text-white rounded">Dùng thử miễn phí</button>
        <button className="ml-2 px-6 py-3 border rounded">Xem thêm</button>
      </div>
    </div>
  );
};

export default HeroSection;