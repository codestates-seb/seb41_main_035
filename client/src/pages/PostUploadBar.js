import { useState } from 'react';
import styled from 'styled-components';
import Dropdown from '../components/Dropdown';
import ItemImageInput from '../components/ItemImageInput';

const PostUploadBar = () => {
  const [brandname, setBrandname] = useState('');
  const [itemName, setItemName] = useState('');
  const [itemSize, setItemSize] = useState('');
  const [itemPrice, setItemPrice] = useState('');
  const [itemSite, setItemSite] = useState('');
  const [rentalCheck, setRentalCheck] = useState(false);
  const [rentalPrice, setRentalPrice] = useState('');

  const onChangeBrandname = (e) => {
    setBrandname(e.target.value);
  };
  const onChangeName = (e) => {
    setItemName(e.target.value);
  };
  const onChangeSize = (e) => {
    setItemSize(e.target.value);
  };
  const onChangePrice = (e) => {
    setItemPrice(e.target.value);
  };
  const onChangeSite = (e) => {
    setItemSite(e.target.value);
  };
  const onChangeRentalCheck = () => {
    setRentalCheck((prev) => !prev);
  };
  const onChangeRentalPrice = (e) => {
    setRentalPrice(e.target.value);
  };

  return (
    <SWrapper>
      <SContainer>
        {/* <button className="image-upload">제품 이미지 업로드</button> */}
        <ItemImageInput />
        <span className="category">
          {/* 드롭다운구현하기 */}
          <Dropdown />
        </span>
        <SMidle>
          <div className="item-top">
            <div className="item-brand">
              브랜드
              <input
                type="text"
                placeholder="브랜드"
                onChange={onChangeBrandname}
              ></input>
            </div>
            <div className="item-name">
              제품명
              <input
                type="text"
                placeholder="제품명"
                onChange={onChangeName}
              ></input>
            </div>
          </div>
          <div className="item-mid">
            <div className="item-size">
              사이즈
              <input
                type="text"
                placeholder="사이즈"
                onChange={onChangeSize}
              ></input>
            </div>
            <div className="item-price">
              가격
              <input
                type="text"
                placeholder="가격"
                onChange={onChangePrice}
              ></input>
            </div>
          </div>
          <div className="item-site">
            구매링크
            <input
              type="text"
              placeholder="구매링크"
              onChange={onChangeSite}
            ></input>
          </div>
        </SMidle>
        <SRentalcheck>
          <div className="check">
            렌탈가능체크
            <input type="checkbox" onChange={onChangeRentalCheck}></input>
          </div>
          <div className="rental-price">
            렌탈금액
            <input
              type="text"
              placeholder="렌탈금액"
              onChange={onChangeRentalPrice}
            ></input>
          </div>
        </SRentalcheck>
      </SContainer>
    </SWrapper>
  );
};

const SWrapper = styled.div`
  display: flex;
  justify-content: center;
  width: 100%;

  input {
    width: 6vw;
    border: 1.5px solid #d9d4a6;
    border-radius: 3px;
  }
  .image-upload {
    margin: 10px;
  }
  .category {
    margin: 20px;
    font-size: 12px;
    background-color: #eee6ca;
  }
`;

const SContainer = styled.div`
  width: 44vw;
  /* height: 16vh; */
  background-color: #eee6ca;
  /* flex-direction: row; */
  display: flex;
  margin: 10px;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 12px;
`;

const SMidle = styled.div`
  font-size: 14px;
  margin: 10px 0px;
  width: 20vw;
  background-color: #eee6ca;

  .item-top {
    display: flex;
    margin: 10px 10px 15px 10px;
    background-color: #eee6ca;
    .item-brand {
      background-color: #eee6ca;
    }
    .item-name {
      margin-left: 15px;
      background-color: #eee6ca;
    }
  }
  .item-top input {
    margin-left: 10px;
  }

  .item-mid {
    display: flex;
    margin: 10px 10px 15px 10px;
    background-color: #eee6ca;
    .item-size {
      background-color: #eee6ca;
    }
  }
  .item-mid input {
    margin-left: 10px;
  }

  .item-price {
    margin-left: 15px;
    background-color: #eee6ca;
  }
  .item-price > input {
    margin-left: 22px;
  }

  .item-site {
    margin-left: 10px;
    background-color: #eee6ca;
  }
  .item-site > input {
    margin-left: 10px;
    width: 15vw;
  }
`;

const SRentalcheck = styled.div`
  font-size: 14px;
  margin: 10px;
  background-color: #eee6ca;
  .check {
    margin: 20px 0px;
    display: flex;
    align-items: center;
    background-color: #eee6ca;
  }
  .check > input {
    width: 1.8vw;
    height: 1.8vh;
  }
  .rental-price {
    background-color: #eee6ca;
  }
  .rental-price > input {
    margin-left: 10px;
    width: 5vw;
  }
`;

export default PostUploadBar;
