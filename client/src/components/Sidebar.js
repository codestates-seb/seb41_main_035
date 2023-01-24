import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
const Sidebar = () => {
  const usenavigate = useNavigate();

  return (
    <SWrapper>
      <SidebarBox>
        <div>
          <p className="title">카테고리</p>
        </div>
        <ItemList>
          <div>
            <p
              className="items"
              role="presentation"
              onClick={() => {
                usenavigate(`/category/outer`);
              }}
            >
              아우터
            </p>
            <p
              className="items"
              role="presentation"
              onClick={() => {
                usenavigate(`/category/top`);
              }}
            >
              상의
            </p>
            <p
              className="items"
              role="presentation"
              onClick={() => {
                usenavigate(`/category/bottom`);
              }}
            >
              하의
            </p>
            <p
              className="items"
              role="presentation"
              onClick={() => {
                usenavigate(`/category/onepiece`);
              }}
            >
              원피스
            </p>
            <p
              className="items"
              role="presentation"
              onClick={() => {
                usenavigate(`/category/hat`);
              }}
            >
              모자
            </p>
            <p
              className="items"
              role="presentation"
              onClick={() => {
                usenavigate(`/category/shoes`);
              }}
            >
              신발
            </p>
          </div>
        </ItemList>
      </SidebarBox>
      <SLeftBoard />
    </SWrapper>
  );
};

//전체부분
const SWrapper = styled.div`
  position: fixed;
  display: flex;
  width: 18%;
  justify-content: flex-end;
  min-width: 110px;
`;

const SidebarBox = styled.div`
  /* position: fixed; */
  .title {
    font-size: 20px;
    font-weight: 700;
    color: #4e4e4e;
    padding: 40px 30px 20px 0px;
    font-family: 'Gowun Batang', serif;
  }
`;
const ItemList = styled.div`
  .items {
    font-size: 15px;
    cursor: pointer;
    padding: 10px 3px;
    :hover {
      color: #196ba5;
      background-color: #f4f1e0;
    }
  }
`;
const SLeftBoard = styled.div`
  position: fixed;
  border-left: 2px solid gray;
  margin: 50px 0px 0px 20px;
  height: 400px;
`;
export default Sidebar;
