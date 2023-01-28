import { useState } from 'react';
import styled from 'styled-components';
import Dropdown from '../components/Dropdown';
import ItemImageInput from '../components/ItemImageInput';
import PropTypes from 'prop-types';
import { token, BREAK_POINT_PC, BREAK_POINT_TABLET } from '../constants/index';
const PostUploadBar = ({ index, onChangeItem }) => {
  const [imgFile, setImgFile] = useState([]); // 이미지 배열
  const [brandname, setBrandname] = useState('');
  const [itemName, setItemName] = useState('');
  const [itemSize, setItemSize] = useState('');
  const [itemPrice, setItemPrice] = useState('');
  const [itemSite, setItemSite] = useState('');
  const [rentalCheck, setRentalCheck] = useState(false);
  const [rentalPrice, setRentalPrice] = useState('');
  //-----추가부분 드롭다운.-----------
  const [itemDropdown, setItemDropdown] = useState('');

  // 미리보기 이미지를 저장
  const saveImagePreviewLinks = (imageUrlLists) => {
    setImgFile(imageUrlLists);
  };

  // 이미지 업데이트 함수
  const onUploadImages = (imageFileList) => {
    //서버에 전달할 File 객체를 저장
    setImgFile(imageFileList);
    onChangeItem(index, 'productImage', imageFileList); //상위 컴포넌트인 PostUpload의 contentList State가 업데이트
  };

  const onChangeBrandname = (e) => {
    setBrandname(e.target.value);
    onChangeItem(index, 'brand', e.target.value);
  };
  const onChangeName = (e) => {
    setItemName(e.target.value);
    onChangeItem(index, 'productName', e.target.value);
  };
  const onChangeSize = (e) => {
    setItemSize(e.target.value);
    onChangeItem(index, 'size', e.target.value);
  };
  const onChangePrice = (e) => {
    setItemPrice(e.target.value);
    onChangeItem(index, 'price', e.target.value);
  };
  const onChangeSite = (e) => {
    setItemSite(e.target.value);
    onChangeItem(index, 'link', e.target.value);
  };
  const onChangeRentalCheck = () => {
    setRentalCheck(true);
    onChangeItem(index, 'rental', true);
  };
  const onChangeRentalPrice = (e) => {
    setRentalPrice(e.target.value);
    onChangeItem(index, 'rentalPrice', e.target.value);
  };

  //추가!!!
  const onChangeDropdown = (e) => {
    setItemDropdown(e.target.value);
    onChangeItem(index, 'category', e.target.value);
  };

  return (
    <SWrapper>
      <SContainer>
        {/* 제품 이미지 업로드*/}
        <ItemImageInput
          index={index}
          imgFile={imgFile}
          onUploadImages={onUploadImages}
          saveImagePreviewLinks={saveImagePreviewLinks}
        />
        <span className="category">
          {/* 드롭다운 ---------------추가부분------------------*/}
          <Dropdown
            onChangeDropdown={onChangeDropdown}
            itemDropdown={itemDropdown}
          />
        </span>
        <SMidle>
          <div className="item-top">
            <div className="item-brand">
              브랜드
              <input
                type="text"
                placeholder="브랜드"
                value={brandname}
                onChange={onChangeBrandname}
              ></input>
            </div>
            <div className="item-name">
              제품명
              <input
                type="text"
                placeholder="제품명"
                value={itemName}
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
                value={itemSize}
                onChange={onChangeSize}
              ></input>
            </div>
            <div className="item-price">
              가격
              <input
                type="text"
                placeholder="가격"
                value={itemPrice}
                onChange={onChangePrice}
              ></input>
            </div>
          </div>
          <div className="item-site">
            구매링크
            <input
              type="text"
              placeholder="구매링크"
              value={itemSite}
              onChange={onChangeSite}
            ></input>
          </div>
        </SMidle>
        <SRentalcheck>
          <div className="check">
            렌탈가능체크
            <input
              type="checkbox"
              value={rentalCheck}
              onChange={onChangeRentalCheck}
            ></input>
          </div>
          <div className="rental-price">
            렌탈금액
            <input
              type="text"
              placeholder="렌탈금액"
              value={rentalPrice}
              onChange={onChangeRentalPrice}
            ></input>
          </div>
        </SRentalcheck>
      </SContainer>
    </SWrapper>
  );
};

PostUploadBar.propTypes = {
  index: PropTypes.number,
  onChangeItem: PropTypes.func,
};

const SWrapper = styled.div`
  display: flex;
  justify-content: center;

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
  height: 150px;
  background-color: #eee6ca;
  display: flex;
  justify-content: space-around;
  align-items: baseline;
  margin: 10px 0px; //* 추가
  box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 12px;
  border-radius: 6px;
  /* @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
    height: 200px;
  } */
`;

const SMidle = styled.div`
  font-size: 14px;
  /* margin: 10px 0px; */
  /* width: 350px; */
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
    margin: 10px 0px;
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
