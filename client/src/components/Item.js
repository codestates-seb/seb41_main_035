import { ReactComponent as Rent } from '../svg/Rent.svg';
import styled from 'styled-components';
import dummyData from '../db/dummyData.json';
import Avatar from '../components/Avatar';
const Item = () => {
  const data = dummyData.posts;
  return (
    <SItemContainer>
      {data.map((item) => (
        <div className="item_box" key={item.id}>
          <div className="item_picture">
            <Avatar />
          </div>
          <div className="item_info">
            <div className="item_sale">
              <div className="item_info_name">
                <span>브랜드</span>
                <span>제품명</span>
                <span>가격</span>
              </div>
              <div className="item_info_content">
                <span> 나이키</span>
                <span> 나이키</span>
                <span> 2,000원</span>
              </div>
              {/* <div className="item_brand">브랜드 나이키</div>
              <div className="item_name">제품명  나이키</div>
              <div className="item_price">가격 2,000원</div> */}
            </div>
            {/* {isRent ? ( */}
            <div className="item_rent">
              <Rent />
              <div className="rent_price">
                <p>대여가격: </p>
                <p>1,000</p>
              </div>
            </div>
            {/* ) : null} */}
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
