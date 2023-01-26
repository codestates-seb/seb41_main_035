import { ReactComponent as Rent } from '../svg/Rent.svg';
import styled from 'styled-components';
import Avatar from '../components/Avatar';
import { useParams, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import axios from 'axios';
const Item = () => {
  const navigate = useNavigate();
  const params = useParams();
  const url = 'http://13.125.30.88';
  const [itemData, setItemData] = useState([]);
  const [isRent, setIsRent] = useState(true);

  // console.log(itemData.products[1].rental);
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(url + `/boards/` + [params.boardId]);
        setItemData(response.data);
      } catch (err) {
        return err;
      }
    };
    fetchData();
  }, []);
  // Object.keys(itemData.products.rental)?.forEach((el) =>
  //   console.log(el[rental])
  // );
  // console.log(Object.keys(itemData.products.rental));

  const onMoveLink = (link) => {
    window.open(link, '', '');
  };

  function openPop() {
    let popup = window.open(
      'http://www.naver.com',
      '네이버팝업',
      'width=700px,height=800px,scrollbars=yes'
    );
  }
  return (
    <SItemContainer>
      {itemData.products?.map((item) => (
        <div
          className="item_box"
          key={item.productId}
          role="presentation"
          onClick={() => {
            onMoveLink(item.link);
          }}
        >
          <div className="item_picture">
            <Avatar image={item.productImage} />
          </div>
          <div className="item_info">
            <div className="item_sale">
              <div className="item_info_name">
                <span>브랜드</span>
                <span>제품명</span>
                <span>가격</span>
              </div>
              <div className="item_info_content">
                <span> {item.brand}</span>
                <span> {item.productName}</span>
                <span> {item.price}</span>
              </div>
            </div>
            {isRent ? (
              <div className="item_rent">
                <Rent />
                <div className="rent_price">
                  <p>대여가격: </p>
                  <p>{item.rental?.rentalPrice}</p>
                </div>
              </div>
            ) : null}
          </div>
        </div>
      ))}
    </SItemContainer>
  );
};
const SItemContainer = styled.div`
  overflow: auto;
  height: 110px;
  margin-bottom: 10px;

  .item_box {
    height: 85px;
    margin-bottom: 13px;
    display: flex;
    align-items: center;
    padding: 10px;
    background-color: #eee6ca;
    cursor: pointer;
    .item_info {
      width: 90%;
      display: flex;
      justify-content: space-between;
      margin-left: 10px;
      background-color: #eee6ca;
      .item_sale {
        display: flex;
        justify-content: center;
        background-color: #eee6ca;
        .item_info_name {
          display: flex;
          flex-direction: column;
          justify-content: center;
          font-weight: bold;
          text-align: center;
          margin-right: 10px;
          background-color: #eee6ca;
          span {
            background-color: #eee6ca;
          }
        }
        .item_info_content {
          display: flex;
          flex-direction: column;
          justify-content: center;
          font-size: 15px;
          margin-top: 2px;
          background-color: #eee6ca;
          span {
            background-color: #eee6ca;
          }
        }
      }
      .item_rent {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        background-color: #eee6ca;
        svg {
          background-color: #eee6ca;
        }
      }
      .rent_price {
        display: flex;
        background-color: #eee6ca;
        p {
          background-color: #eee6ca;
        }
      }
    }
  }
  .item_picture {
    width: 80px;
    height: 80px;
    object-fit: cover;
    position: relative;
    overflow: hidden;
    img {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      width: 100%;
      height: 100%;
    }
  }
`;
export default Item;
