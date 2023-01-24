import { create } from 'zustand';

const memberstore = create((set) => ({
  isLogin: false,
  setisLogin: (e) => {
    set({
      isLogin: e,
    });
  },
}));

export default memberstore;
