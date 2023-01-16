import styled from 'styled-components';
import Dropdown from '../components/Dropdown';

const PostUploadBar = () => {
  //   const [selectedDropValue, setSelectedDropValue] = useState([
  //     '카테고리를 선택하세요.',
  //   ]);
  return (
    <>
      <SWapper>
        <div className="Qb-contianer">
          <button className="image-upload">제품 이미지 업로드</button>
          <span className="category">
            <Dropdown />
          </span>
          <SMidle>
            <div className="itemname">
              제품명
              <input type="text" size="10" placeholder="제품명"></input>
            </div>
            <div className="price">
              가격
              <input type="text" size="10" placeholder="가격"></input>
            </div>
          </SMidle>
          <SMid>
            <div className="size">
              사이즈
              <input type="text" size="10" placeholder="사이즈"></input>
            </div>
            <div className="site">
              구매링크
              <input type="text" size="10" placeholder="구매링크"></input>
            </div>
          </SMid>
          <SRentalcheck>
            <div className="check">
              렌탈가능체크
              <input type="checkbox"></input>
            </div>
            <div className="rentalprice">
              렌탈금액
              <input type="text" size="10" placeholder="렌탈금액"></input>
            </div>
          </SRentalcheck>
        </div>
      </SWapper>
    </>
  );
};
const SWapper = styled.div`
  display: flex;
  justify-content: center;

  .Qb-contianer {
    width: 55%;
    height: 14vh;
    background-color: #d9d9d9;
    /* flex-direction: row; */
    display: flex;

    button {
      margin: 20px 0px;
      width: 7vw;
      height: 3vh;
    }
  }
  .image-upload {
    margin: 10px;
  }
  .category {
    margin: 20px;
    font-size: 12px;
  }
`;
const SMidle = styled.div`
  font-size: 14px;
  margin: 10px;

  .itemname {
    margin: 20px 0px;
  }
  .price {
    margin-bottom: 2px;
  }
`;
const SMid = styled.div`
  font-size: 14px;
  margin: 10px;

  .size {
    margin: 20px 0px;
  }
  .title {
  }
`;

const SRentalcheck = styled.div`
  font-size: 14px;
  margin: 10px;
  padding-left: 20px;
  .check {
    margin: 20px 0px;
  }
  .checkbox {
    width: 20px;
    height: 20px;
  }
`;

export default PostUploadBar;
