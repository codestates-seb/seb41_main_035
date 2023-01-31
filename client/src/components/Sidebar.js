import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import { BREAK_POINT_TABLET } from '../constants/index';
const Items = ['Outer', 'Top', 'Bottom', 'Onepiece', 'Hat', 'Shoes'];
const links = ['outer', 'top', 'bottom', 'onepiece', 'hat', 'shoes'];
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
            {Items.map((item, index) => (
              <SList
                key={index}
                className="items"
                onClick={() => usenavigate(`/category/${links[index]}`)}
              >
                {item}
              </SList>
            ))}
          </div>
        </ItemList>
      </SidebarBox>
      <SLeftBoard />
    </SWrapper>
  );
};

const SWrapper = styled.div`
  position: fixed;
  display: flex;
  width: 20%;
  justify-content: flex-end;
  min-width: 110px;
  /* 768px이하일때 사이드바 없애기 */
  @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
    display: none;
    visibility: hidden;
  }
`;

//전체부분
const SidebarBox = styled.div`
  /* position: fixed; */
  .title {
    font-size: 20px;
    font-weight: 700;
    color: #4e4e4e;
    padding: 40px 30px 20px 0px;
    font-family: 'Gothic A1', sans-serif;
  }
`;
const ItemList = styled.div`
  border-right: 2px solid gray;

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
const SList = styled.div``;

//선 부분
const SLeftBoard = styled.div`
  position: fixed;
  border-left: 2px solid gray;
  margin: 50px 0px 0px 110px;
  height: 340px;
`;
export default Sidebar;
